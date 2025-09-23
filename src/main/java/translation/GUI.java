package translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // --- Load converters from text files ---
            CountryCodeConverter countryConverter = new CountryCodeConverter("country-codes.txt");
            LanguageCodeConverter languageConverter = new LanguageCodeConverter("language-codes.txt");

            // --- Translator using JSON dataset ---
            Translator translator = new JSONTranslator("sample.json");

            // --- Language panel (dropdown) ---
            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));

            JComboBox<String> languageComboBox = new JComboBox<>();
            for (String lang : languageConverter.getAllLanguageNames()) {
                languageComboBox.addItem(lang);
            }
            languagePanel.add(languageComboBox);

            // --- Country panel (scrollable list) ---
            JPanel countryPanel = new JPanel();
            countryPanel.add(new JLabel("Country:"));

            JList<String> countryList = new JList<>(
                    countryConverter.getAllCountryNames().toArray(new String[0])
            );
            countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane countryScrollPane = new JScrollPane(countryList);
            countryScrollPane.setPreferredSize(new Dimension(200, 120));
            countryPanel.add(countryScrollPane);

            // --- Result panel ---
            JPanel resultPanel = new JPanel();
            JLabel resultLabelText = new JLabel("Translation:");
            JLabel resultLabel = new JLabel(" ");
            resultPanel.add(resultLabelText);
            resultPanel.add(resultLabel);

            // --- Shared update logic ---
            Runnable updateTranslation = () -> {
                String countryName = countryList.getSelectedValue();             // from JList
                String langName    = (String) languageComboBox.getSelectedItem(); // from ComboBox
                if (countryName == null || langName == null) return;

                String countryCode = countryConverter.getCountryCode(countryName);
                String langCode    = languageConverter.getLanguageCode(langName);

                if (countryCode != null && langCode != null) {
                    // normalize to lowercase to match sample.json
                    countryCode = countryCode.toLowerCase();
                    langCode    = langCode.toLowerCase();

                    String result = translator.translate(countryCode, langCode);
                    resultLabel.setText(result != null ? result : "no translation found!");
                }
            };

            // --- Listeners ---
            countryList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) updateTranslation.run();
            });

            languageComboBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) updateTranslation.run();
            });

            // --- Main Panel (language first, then country, then result) ---
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel); // top: language dropdown
            mainPanel.add(countryPanel);  // middle: country list
            mainPanel.add(resultPanel);   // bottom: translation output

            // --- Frame ---
            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
