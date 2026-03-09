package com.careerexpert.ui;

import com.careerexpert.engine.JavaInferenceEngine;
import com.careerexpert.engine.PrologBridge;
import com.careerexpert.model.CareerResult;
import com.careerexpert.model.UserProfile;
import com.careerexpert.util.ReportExporter;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Main Java Swing GUI for the Career Expert System.
 *
 * Architecture:
 *   MainWindow → Questionnaire Panel → UserProfile → PrologBridge/JavaEngine → ResultsPanel
 */
public class MainWindow extends JFrame {

    // ==================== UI Components ====================
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private WelcomePanel welcomePanel;
    private QuestionnairePanel questionnairePanel;
    private ResultsPanel resultsPanel;

    // Colors
    static final Color PRIMARY   = new Color(26, 35, 126);   // Indigo 900
    static final Color SECONDARY = new Color(40, 53, 147);   // Indigo 800
    static final Color ACCENT    = new Color(21, 101, 192);  // Blue 800
    static final Color LIGHT_BG  = new Color(232, 240, 254);
    static final Color WHITE     = Color.WHITE;

    // Engine
    private final JavaInferenceEngine inferenceEngine = new JavaInferenceEngine();

    public MainWindow() {
        initUI();
    }

    private void initUI() {
        setTitle("🎓 Career Path & Course Recommender System");
        setSize(900, 700);
        setMinimumSize(new Dimension(750, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Setup card layout for screen navigation
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        welcomePanel = new WelcomePanel(this);
        questionnairePanel = new QuestionnairePanel(this);
        resultsPanel = new ResultsPanel(this);

        cardPanel.add(welcomePanel, "WELCOME");
        cardPanel.add(questionnairePanel, "QUESTIONNAIRE");
        cardPanel.add(resultsPanel, "RESULTS");

        add(cardPanel);
        showScreen("WELCOME");
    }

    void showScreen(String name) {
        cardLayout.show(cardPanel, name);
    }

    void runInference(UserProfile profile) {
        // Show loading cursor
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        // Run in background thread
        SwingWorker<List<CareerResult>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<CareerResult> doInBackground() {
                return inferenceEngine.evaluate(profile, 3);
            }

            @Override
            protected void done() {
                try {
                    List<CareerResult> results = get();
                    resultsPanel.displayResults(profile, results);
                    showScreen("RESULTS");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(MainWindow.this,
                            "Error during inference: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}


// ============================================================
// WELCOME PANEL
// ============================================================
class WelcomePanel extends JPanel {

    WelcomePanel(MainWindow parent) {
        setLayout(new BorderLayout());
        setBackground(MainWindow.PRIMARY);

        // Center content
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(MainWindow.PRIMARY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        // Logo / Title
        JLabel icon = new JLabel("🎓", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        icon.setForeground(Color.WHITE);
        center.add(icon, gbc);

        JLabel title = new JLabel("Career Path & Course", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        center.add(title, gbc);

        JLabel title2 = new JLabel("Recommender System", SwingConstants.CENTER);
        title2.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title2.setForeground(new Color(179, 212, 252));
        center.add(title2, gbc);

        gbc.insets = new Insets(4, 20, 4, 20);
        JLabel subtitle = new JLabel("Rule-Based Expert System  •  Powered by Prolog + Java", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        subtitle.setForeground(new Color(147, 179, 224));
        center.add(subtitle, gbc);

        gbc.insets = new Insets(30, 40, 10, 40);
        JLabel desc = buildWrappedLabel("<html><center>Answer a short questionnaire about your interests,<br>" +
                "subjects, and skills — and get your personalized<br>" +
                "Top 3 Career Recommendations with explanations.</center></html>");
        desc.setForeground(new Color(200, 220, 255));
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        center.add(desc, gbc);

        gbc.insets = new Insets(20, 40, 8, 40);
        JButton startBtn = createStyledButton("Get Started →", new Color(21, 101, 192), Color.WHITE);
        startBtn.setPreferredSize(new Dimension(220, 50));
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        startBtn.addActionListener(e -> parent.showScreen("QUESTIONNAIRE"));
        center.add(startBtn, gbc);

        add(center, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel("Expert System  |  Forward Chaining Inference  |  24 Career Rules", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(100, 130, 190));
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        add(footer, BorderLayout.SOUTH);
    }

    private JLabel buildWrappedLabel(String html) {
        return new JLabel(html, SwingConstants.CENTER);
    }

    static JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}


// ============================================================
// QUESTIONNAIRE PANEL
// ============================================================
class QuestionnairePanel extends JPanel {

    private final MainWindow parent;

    // Interests checkboxes
    private final JCheckBox[] interestBoxes;
    private final String[] interests = {"Technology", "Business", "Health", "Arts", "Engineering"};

    // Subjects checkboxes
    private final JCheckBox[] subjectBoxes;
    private final String[] subjects = {"Mathematics", "Biology", "Chemistry", "Physics", "Economics", "Literature", "History"};

    // Skills checkboxes
    private final JCheckBox[] skillBoxes;
    private final String[] skills = {
        "Problem Solving", "Analytical Thinking", "Creativity", "Communication",
        "Leadership", "Helping People", "Attention to Detail", "Visual Arts",
        "Empathy", "Technical Skills", "Business Acumen", "Mathematics"
    };

    // Environment dropdown
    private final JComboBox<String> environmentCombo;

    // Education dropdown
    private final JComboBox<String> educationCombo;

    QuestionnairePanel(MainWindow parent) {
        this.parent = parent;

        // Initialize checkboxes
        interestBoxes = new JCheckBox[interests.length];
        subjectBoxes  = new JCheckBox[subjects.length];
        skillBoxes    = new JCheckBox[skills.length];

        for (int i = 0; i < interests.length; i++) interestBoxes[i] = new JCheckBox(interests[i]);
        for (int i = 0; i < subjects.length;  i++) subjectBoxes[i]  = new JCheckBox(subjects[i]);
        for (int i = 0; i < skills.length;    i++) skillBoxes[i]    = new JCheckBox(skills[i]);

        environmentCombo = new JComboBox<>(new String[]{
                "-- Select --", "Office", "Hospital", "Outdoor", "Remote", "School", "Laboratory"});
        educationCombo = new JComboBox<>(new String[]{
                "-- Select --", "High School", "Undergraduate", "Postgraduate", "PhD"});

        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(MainWindow.LIGHT_BG);

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(MainWindow.PRIMARY);
        topBar.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        JLabel topTitle = new JLabel("📋  Questionnaire", SwingConstants.LEFT);
        topTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        topTitle.setForeground(Color.WHITE);
        topBar.add(topTitle, BorderLayout.WEST);
        JLabel step = new JLabel("Step 1 of 2", SwingConstants.RIGHT);
        step.setForeground(new Color(180, 200, 255));
        step.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        topBar.add(step, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Scrollable form
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(MainWindow.LIGHT_BG);
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        form.add(buildSection("1.  What are your main interests?", interestBoxes, 5));
        form.add(Box.createVerticalStrut(16));
        form.add(buildSection("2.  What are your strongest subjects?", subjectBoxes, 7));
        form.add(Box.createVerticalStrut(16));
        form.add(buildSection("3.  What are your key skills?", skillBoxes, 6));
        form.add(Box.createVerticalStrut(16));
        form.add(buildDropdownSection("4.  Preferred work environment:", environmentCombo));
        form.add(Box.createVerticalStrut(16));
        form.add(buildDropdownSection("5.  Current education level:", educationCombo));

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        add(scroll, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 210, 240)));

        JButton clearBtn = WelcomePanel.createStyledButton("Clear", new Color(200, 210, 230), MainWindow.PRIMARY);
        clearBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        clearBtn.setPreferredSize(new Dimension(100, 42));
        clearBtn.addActionListener(e -> clearAll());

        JButton backBtn = WelcomePanel.createStyledButton("← Back", new Color(220, 225, 245), MainWindow.PRIMARY);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setPreferredSize(new Dimension(100, 42));
        backBtn.addActionListener(e -> parent.showScreen("WELCOME"));

        JButton analyzeBtn = WelcomePanel.createStyledButton("Analyze My Profile →", MainWindow.ACCENT, Color.WHITE);
        analyzeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        analyzeBtn.setPreferredSize(new Dimension(200, 42));
        analyzeBtn.addActionListener(e -> analyze());

        btnPanel.add(clearBtn);
        btnPanel.add(backBtn);
        btnPanel.add(analyzeBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JPanel buildSection(String title, JCheckBox[] boxes, int cols) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 215, 250), 1, true),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(MainWindow.PRIMARY);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(lbl, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, cols, 6, 4));
        grid.setBackground(Color.WHITE);
        for (JCheckBox cb : boxes) {
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            cb.setBackground(Color.WHITE);
            grid.add(cb);
        }
        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildDropdownSection(String title, JComboBox<String> combo) {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 215, 250), 1, true),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(MainWindow.PRIMARY);
        lbl.setPreferredSize(new Dimension(270, 30));
        panel.add(lbl, BorderLayout.WEST);

        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setPreferredSize(new Dimension(220, 30));
        panel.add(combo, BorderLayout.CENTER);
        return panel;
    }

    private void clearAll() {
        for (JCheckBox cb : interestBoxes) cb.setSelected(false);
        for (JCheckBox cb : subjectBoxes)  cb.setSelected(false);
        for (JCheckBox cb : skillBoxes)    cb.setSelected(false);
        environmentCombo.setSelectedIndex(0);
        educationCombo.setSelectedIndex(0);
    }

    private void analyze() {
        UserProfile profile = new UserProfile();

        for (int i = 0; i < interestBoxes.length; i++)
            if (interestBoxes[i].isSelected()) profile.addInterest(interests[i]);

        for (int i = 0; i < subjectBoxes.length; i++)
            if (subjectBoxes[i].isSelected()) profile.addSubject(subjects[i]);

        for (int i = 0; i < skillBoxes.length; i++)
            if (skillBoxes[i].isSelected()) profile.addSkill(skills[i]);

        String env = (String) environmentCombo.getSelectedItem();
        if (env != null && !env.startsWith("--")) profile.setPreferredEnvironment(env);

        String edu = (String) educationCombo.getSelectedItem();
        if (edu != null && !edu.startsWith("--")) profile.setEducationLevel(edu);

        if (!profile.hasEnoughData()) {
            JOptionPane.showMessageDialog(parent,
                    "Please select at least 2 items (interests, subjects, or skills) to get recommendations.",
                    "More Input Needed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        parent.runInference(profile);
    }
}


// ============================================================
// RESULTS PANEL
// ============================================================
class ResultsPanel extends JPanel {

    private final MainWindow parent;
    private JPanel resultsContainer;
    private JLabel profileSummaryLabel;
    private UserProfile currentProfile;
    private List<CareerResult> currentResults;

    ResultsPanel(MainWindow parent) {
        this.parent = parent;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(MainWindow.LIGHT_BG);

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(MainWindow.PRIMARY);
        topBar.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        JLabel topTitle = new JLabel("🏆  Your Career Recommendations", SwingConstants.LEFT);
        topTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        topTitle.setForeground(Color.WHITE);
        topBar.add(topTitle, BorderLayout.WEST);
        JLabel step = new JLabel("Step 2 of 2", SwingConstants.RIGHT);
        step.setForeground(new Color(180, 200, 255));
        step.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        topBar.add(step, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Profile summary bar
        profileSummaryLabel = new JLabel("", SwingConstants.LEFT);
        profileSummaryLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        profileSummaryLabel.setForeground(new Color(60, 80, 140));
        profileSummaryLabel.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        JPanel summaryBar = new JPanel(new BorderLayout());
        summaryBar.setBackground(new Color(224, 232, 255));
        summaryBar.add(profileSummaryLabel);
        add(summaryBar, BorderLayout.AFTER_LINE_ENDS);

        // Results container
        resultsContainer = new JPanel();
        resultsContainer.setLayout(new BoxLayout(resultsContainer, BoxLayout.Y_AXIS));
        resultsContainer.setBackground(MainWindow.LIGHT_BG);
        resultsContainer.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        JScrollPane scroll = new JScrollPane(resultsContainer);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        add(scroll, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 12));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 210, 240)));

        JButton backBtn = WelcomePanel.createStyledButton("← Redo Questionnaire", new Color(220, 225, 245), MainWindow.PRIMARY);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backBtn.setPreferredSize(new Dimension(200, 42));
        backBtn.addActionListener(e -> parent.showScreen("QUESTIONNAIRE"));

        JButton exportBtn = WelcomePanel.createStyledButton("💾  Export Report", new Color(46, 125, 50), Color.WHITE);
        exportBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        exportBtn.setPreferredSize(new Dimension(160, 42));
        exportBtn.addActionListener(e -> exportReport());

        btnPanel.add(backBtn);
        btnPanel.add(exportBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    void displayResults(UserProfile profile, List<CareerResult> results) {
        this.currentProfile = profile;
        this.currentResults = results;

        // Update profile summary
        profileSummaryLabel.setText("<html><b>Profile:</b> Interests: " +
                String.join(", ", profile.getInterests()) + "  |  Skills: " +
                String.join(", ", profile.getSkills()) + "</html>");

        // Clear old results
        resultsContainer.removeAll();

        if (results.isEmpty()) {
            JLabel none = new JLabel("No careers matched your profile. Try selecting more options.", SwingConstants.CENTER);
            none.setFont(new Font("Segoe UI", Font.ITALIC, 15));
            none.setForeground(new Color(100, 100, 120));
            resultsContainer.add(none);
        } else {
            for (CareerResult r : results) {
                resultsContainer.add(buildResultCard(r));
                resultsContainer.add(Box.createVerticalStrut(16));
            }
        }

        resultsContainer.revalidate();
        resultsContainer.repaint();
    }

    private JPanel buildResultCard(CareerResult r) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(147, 179, 244), 1, true),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        // Header row
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel rankLabel = new JLabel("#" + r.getRank() + "  ");
        rankLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        rankLabel.setForeground(MainWindow.ACCENT);

        JLabel nameLabel = new JLabel(r.getCareerName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(MainWindow.PRIMARY);

        JLabel matchLabel = new JLabel(" " + r.getMatchPercent() + "% Match ");
        matchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        matchLabel.setForeground(Color.WHITE);
        matchLabel.setBackground(MainWindow.ACCENT);
        matchLabel.setOpaque(true);
        matchLabel.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));

        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nameRow.setBackground(Color.WHITE);
        nameRow.add(rankLabel);
        nameRow.add(nameLabel);
        nameRow.add(Box.createHorizontalStrut(12));
        nameRow.add(matchLabel);

        header.add(nameRow, BorderLayout.WEST);
        card.add(header, BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel(new GridLayout(0, 1, 0, 5));
        body.setBackground(Color.WHITE);
        body.add(buildRow("📄 Description", r.getDescription()));
        body.add(buildRow("🎓 Degree", r.getSuggestedDegree()));
        body.add(buildRow("📊 Outlook", r.getCareerOutlook()));
        body.add(buildRow("📜 Certifications", String.join(", ", r.getCertifications())));
        card.add(body, BorderLayout.CENTER);

        // Explanation
        JPanel expPanel = new JPanel(new BorderLayout());
        expPanel.setBackground(new Color(255, 253, 231));
        expPanel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 4, 1, 1, new Color(249, 168, 37)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        JLabel expTitle = new JLabel("💡 Why this career?");
        expTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        expTitle.setForeground(new Color(130, 90, 0));
        JLabel expText = new JLabel("<html>" + r.getExplanation() + "</html>");
        expText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        expText.setForeground(new Color(80, 60, 0));
        expPanel.add(expTitle, BorderLayout.NORTH);
        expPanel.add(expText, BorderLayout.CENTER);
        card.add(expPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel buildRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(label + ":  ");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(80, 100, 160));
        lbl.setPreferredSize(new Dimension(160, 20));
        JLabel val = new JLabel("<html>" + value + "</html>");
        val.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        val.setForeground(new Color(40, 40, 60));
        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);
        return row;
    }

    private void exportReport() {
        if (currentProfile == null || currentResults == null) return;

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Report");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showSaveDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            File outputDir = chooser.getSelectedFile();
            try {
                String htmlFile = ReportExporter.exportToHtml(currentProfile, currentResults, outputDir.getAbsolutePath());
                String txtFile  = ReportExporter.exportToText(currentProfile, currentResults, outputDir.getAbsolutePath());
                JOptionPane.showMessageDialog(parent,
                        "Reports saved:\n  HTML: " + htmlFile + "\n  TXT:  " + txtFile,
                        "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent,
                        "Failed to save report: " + ex.getMessage(),
                        "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
