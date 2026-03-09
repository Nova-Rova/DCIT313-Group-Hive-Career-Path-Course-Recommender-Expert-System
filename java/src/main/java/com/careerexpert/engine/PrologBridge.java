package com.careerexpert.engine;

import com.careerexpert.model.CareerResult;
import com.careerexpert.model.UserProfile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * PrologBridge manages communication between Java and SWI-Prolog.
 *
 * Architecture:
 *   Java (UI) → PrologBridge → Process(swipl) → knowledge_base.pl
 *                            ← Pipe response ← inference results
 *
 * Protocol:
 *   Java writes: recommend('[fact1,fact2,...]').\n
 *   Prolog writes: RESULT|rank|id|name|score|pct|desc|skills|degree|certs|outlook|explanation
 */
public class PrologBridge {

    private static final Logger LOGGER = Logger.getLogger(PrologBridge.class.getName());
    private static final String PROLOG_EXECUTABLE = "swipl";
    private static final String PROLOG_SERVER_FILE = "prolog/prolog_server.pl";
    private static final String RESULT_PREFIX = "RESULT|";
    private static final String COUNT_PREFIX = "RESULT_COUNT|";

    private Process prologProcess;
    private BufferedWriter prologWriter;
    private BufferedReader prologReader;
    private boolean isConnected = false;

    // ===================== Connection Management =====================

    /**
     * Starts the SWI-Prolog process and establishes communication pipes.
     */
    public void connect(String projectBasePath) throws IOException {
        String serverPath = projectBasePath + File.separator + PROLOG_SERVER_FILE;

        // Verify the Prolog file exists
        File serverFile = new File(serverPath);
        if (!serverFile.exists()) {
            throw new FileNotFoundException("Prolog server not found at: " + serverPath);
        }

        // Start SWI-Prolog as a subprocess
        ProcessBuilder pb = new ProcessBuilder(
                PROLOG_EXECUTABLE,
                "-g", "main",          // run main/0 on startup
                "-t", "halt",          // halt after main returns
                serverPath
        );
        pb.redirectErrorStream(false);
        pb.directory(new File(projectBasePath));

        this.prologProcess = pb.start();
        this.prologWriter = new BufferedWriter(
                new OutputStreamWriter(prologProcess.getOutputStream()));
        this.prologReader = new BufferedReader(
                new InputStreamReader(prologProcess.getInputStream()));

        this.isConnected = true;
        LOGGER.info("Connected to Prolog engine.");
    }

    /**
     * Disconnects from Prolog process.
     */
    public void disconnect() {
        if (prologProcess != null && prologProcess.isAlive()) {
            prologProcess.destroy();
        }
        isConnected = false;
        LOGGER.info("Disconnected from Prolog engine.");
    }

    public boolean isConnected() {
        return isConnected && prologProcess != null && prologProcess.isAlive();
    }

    // ===================== Core Query Methods =====================

    /**
     * Main method: sends user profile to Prolog and gets career recommendations.
     *
     * @param profile The user's collected profile data
     * @param topN    Number of top results to return
     * @return List of CareerResult objects sorted by score descending
     */
    public List<CareerResult> recommend(UserProfile profile, int topN) throws IOException {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected to Prolog. Call connect() first.");
        }

        String factList = profile.toPrologFactList();
        String query = "recommend('" + factList + "').\n";

        LOGGER.info("Sending query to Prolog: " + query.trim());

        // Write query to Prolog
        prologWriter.write(query);
        prologWriter.flush();

        // Read response
        List<CareerResult> results = parseResults(topN);
        LOGGER.info("Received " + results.size() + " results from Prolog.");
        return results;
    }

    // ===================== Fallback: Built-in Java Engine =====================

    /**
     * Fallback inference engine implemented in Java.
     * Used when SWI-Prolog is not installed on the system.
     * Implements the same forward chaining logic as Prolog.
     */
    public List<CareerResult> recommendFallback(UserProfile profile, int topN) {
        LOGGER.warning("Using Java fallback inference engine (Prolog not available).");
        JavaInferenceEngine engine = new JavaInferenceEngine();
        return engine.evaluate(profile, topN);
    }

    // ===================== Response Parser =====================

    private List<CareerResult> parseResults(int topN) throws IOException {
        List<CareerResult> results = new ArrayList<>();
        int expectedCount = -1;
        String line;
        long startTime = System.currentTimeMillis();
        long timeoutMs = 10000; // 10 second timeout

        while ((line = readLineWithTimeout(startTime, timeoutMs)) != null) {
            if (line.startsWith(COUNT_PREFIX)) {
                expectedCount = Integer.parseInt(line.substring(COUNT_PREFIX.length()).trim());
            } else if (line.startsWith(RESULT_PREFIX)) {
                CareerResult result = parseResultLine(line);
                if (result != null && results.size() < topN) {
                    results.add(result);
                }
                // Stop if we have enough or got all
                if (results.size() >= topN || (expectedCount > 0 && results.size() >= expectedCount)) {
                    break;
                }
            } else if (line.startsWith("ERROR|")) {
                LOGGER.warning("Prolog error: " + line);
                break;
            }
        }

        return results;
    }

    private String readLineWithTimeout(long startTime, long timeoutMs) throws IOException {
        while (System.currentTimeMillis() - startTime < timeoutMs) {
            if (prologReader.ready()) {
                return prologReader.readLine();
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return null;
    }

    /**
     * Parses a pipe-delimited result line from Prolog.
     * Format: RESULT|rank|id|name|score|pct|desc|skills|degree|certs|outlook|explanation
     */
    private CareerResult parseResultLine(String line) {
        try {
            String[] parts = line.split("\\|", 12);
            if (parts.length < 12) return null;

            int rank         = Integer.parseInt(parts[1].trim());
            String id        = parts[2].trim();
            String name      = parts[3].trim();
            int score        = Integer.parseInt(parts[4].trim());
            int pct          = Integer.parseInt(parts[5].trim());
            String desc      = parts[6].trim();
            List<String> skills = Arrays.asList(parts[7].trim().split(","));
            String degree    = parts[8].trim();
            List<String> certs = Arrays.asList(parts[9].trim().split(","));
            String outlook   = parts[10].trim();
            String explain   = parts[11].trim();

            return new CareerResult(rank, id, name, score, pct, desc,
                    skills, degree, certs, outlook, explain);

        } catch (Exception e) {
            LOGGER.warning("Failed to parse result line: " + line + " | Error: " + e.getMessage());
            return null;
        }
    }
}
