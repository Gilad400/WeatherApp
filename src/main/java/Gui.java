import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Gui {
    /**
     * Initializes and displays the main weather application interface.
     * Sets up the main frame, panels, input fields, and button with an action listener.
     */
    public static void weatherApp() {
        JFrame frame = createMainFrame();
        JPanel mainPanel = createMainPanel();
        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();
        JButton showWeatherButton = createShowWeatherButton();

        // Add left and right panels to the main panel
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        // Add the main panel and bottom panel to the frame
        frame.add(mainPanel, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel(showWeatherButton);
        JTable weatherTable = createWeatherTable(new String[]{"Hour", "Temperature"}); // Updated to capture weatherTable
        JScrollPane scrollPane = new JScrollPane(weatherTable);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Add action listener to the button
        addShowWeatherAction(showWeatherButton, leftPanel, rightPanel, weatherTable); // Pass weatherTable

        frame.setVisible(true);
    }

    /**
     * Creates and configures the main application frame.
     *
     * @return a configured JFrame with title and layout settings.
     */
    private static JFrame createMainFrame() {
        JFrame frame = new JFrame("Weather App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLayout(new BorderLayout());
        return frame;
    }

    /**
     * Creates the main panel holding the left and right sections for input fields.
     *
     * @return a JPanel with a grid layout.
     */
    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 2));
        return mainPanel;
    }

    /**
     * Sets up the left panel with input fields for city name, year, month, and day.
     *
     * @return a JPanel containing labeled input fields for city, year, month, and day.
     */
    private static JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(4, 2, 5, 5));

        leftPanel.add(new JLabel("City Name:"));
        leftPanel.add(new JTextField());

        leftPanel.add(new JLabel("Year:"));
        leftPanel.add(createYearComboBox());

        leftPanel.add(new JLabel("Month:"));
        leftPanel.add(createMonthComboBox());

        leftPanel.add(new JLabel("Day:"));
        leftPanel.add(createDayComboBox());

        return leftPanel;
    }

    /**
     * Creates a combo box with a range of selectable years starting from the current year.
     *
     * @return a JComboBox populated with year values.
     */
    private static JComboBox<String> createYearComboBox() {
        String[] years = new String[11];
        for (int i = 0; i <= 10; i++) {
            years[i] = Integer.toString(2024 + i);
        }
        return new JComboBox<>(years);
    }

    /**
     * Creates a combo box with month values from 1 to 12.
     *
     * @return a JComboBox populated with month values.
     */
    private static JComboBox<String> createMonthComboBox() {
        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = Integer.toString(i + 1);
        }
        return new JComboBox<>(months);
    }

    /**
     * Creates a combo box with day values from 1 to 31.
     *
     * @return a JComboBox populated with day values.
     */
    private static JComboBox<String> createDayComboBox() {
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = Integer.toString(i + 1);
        }
        return new JComboBox<>(days);
    }

    /**
     * Sets up the right panel with a free-text input field.
     *
     * @return a JPanel containing a label and text field for user input.
     */
    private static JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(new JLabel("Free Text:"), BorderLayout.NORTH);
        rightPanel.add(new JTextField(), BorderLayout.CENTER);
        return rightPanel;
    }

    /**
     * Creates a button to retrieve and display weather data.
     *
     * @return a JButton configured to show weather data.
     */
    private static JButton createShowWeatherButton() {
        return new JButton("Show Weather");
    }

    /**
     * Creates a bottom panel containing the "Show Weather" button.
     *
     * @param showWeatherButton The button that triggers weather data retrieval.
     * @return a JPanel containing the button.
     */
    private static JPanel createBottomPanel(JButton showWeatherButton) {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(showWeatherButton, BorderLayout.NORTH);
        return bottomPanel;
    }

    /**
     * Initializes a table for displaying hourly weather data, with hours and temperature columns.
     *
     * @param columnNames An array containing the column names for the table.
     * @return a JTable with empty temperature values and hours as rows.
     */
    private static JTable createWeatherTable(String[] columnNames) {
        Object[][] data = new Object[24][2];
        for (int i = 0; i < 24; i++) {
            data[i][0] = i + ":00";  // Hours (0:00 to 23:00)
            data[i][1] = "";         // Temperature (to be filled later)
        }
        return new JTable(data, columnNames);
    }

    /**
     * Adds an action listener to the "Show Weather" button to fetch and display weather data.
     *
     * @param showWeatherButton The button to add the listener to.
     * @param leftPanel         The left panel containing city, year, month, and day inputs.
     * @param rightPanel        The right panel containing the free-text field.
     * @param weatherTable      The JTable where weather data will be displayed.
     */
    private static void addShowWeatherAction(JButton showWeatherButton, JPanel leftPanel, JPanel rightPanel, JTable weatherTable) {
        showWeatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField cityTextField = (JTextField) leftPanel.getComponent(1);
                JComboBox<String> yearComboBox = (JComboBox<String>) leftPanel.getComponent(3);
                JComboBox<String> monthComboBox = (JComboBox<String>) leftPanel.getComponent(5);
                JComboBox<String> dayComboBox = (JComboBox<String>) leftPanel.getComponent(7);
                JTextField freeTextField = (JTextField) rightPanel.getComponent(1);

                String city = cityTextField.getText();
                String year = (String) yearComboBox.getSelectedItem();
                String month = (String) monthComboBox.getSelectedItem();
                String day = (String) dayComboBox.getSelectedItem();
                String freeText = freeTextField.getText();

                try {
                    JSONObject results;
                    if (freeText.isEmpty()) {
                        results = get_Specific_Weather(city, year, month, day);
                    } else {
                        results = fetchWeatherFromFreeText(freeText, year);
                    }
                    updateWeatherTable(weatherTable, results);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            /**
             * Retrieves weather data by parsing date and city from a free-text input.
             *
             * @param freeText The user-provided text containing date and city information.
             * @param year     The year as a fallback if not specified in free text.
             * @return A JSONObject containing the weather data for the extracted city and date.
             * @throws IOException If an error occurs while fetching data from the API.
             */
            private JSONObject fetchWeatherFromFreeText(String freeText, String year) throws IOException {
                JSONObject chatGptResponse = getContent(ChatGptApi.chatGpt(freeText));
                String city = chatGptResponse.getString("city");
                String month = chatGptResponse.getString("month");
                String day = chatGptResponse.getString("day");
                return get_Specific_Weather(city, year, month, day);
            }

            /**
             * Populates the weather table with temperature data for each hour.
             *
             * @param weatherTable The table to update with temperature data.
             * @param results The JSON object containing hourly temperature data.
             */
            private void updateWeatherTable(JTable weatherTable, JSONObject results) {
                for (int i = 0; i < 24; i++) {
                    double temp = results.getDouble(String.format("%02d:00", i));
                    weatherTable.setValueAt(temp + "°C", i, 1);
                }
            }
        });
    }
/**
 * A helper method to retrieve specific weather data for a given location and date.
 *
 * @param city  The city name for which to retrieve weather data.
 * @param year  The year as a string (e.g., "2023").
 * @param month The month as a string (e.g., "08" for August).
 * @param day   The day as a string (e.g., "15" for the 15th).
 * @return A JSONObject containing the weather data for the specified city and date, as returned by
 *         the WeatherApi.get_Specific_Weather method.
 * @throws IOException If an error occurs while fetching data from the WeatherApi.
 */
    private static JSONObject get_Specific_Weather(String city, String year, String month, String day) throws IOException {
        return WeatherApi.get_Specific_Weather(city, year, month, day);
    }
/**
 * Extracts the content of a chat response message from the OpenAI GPT model's response JSON.
 *
 * @param chatGptResponse The JSONObject containing the complete response from the OpenAI API.
 * @return A JSONObject parsed from the content of the response message's first choice.
 */
    private static JSONObject getContent(JSONObject chatGptResponse) {
        return new JSONObject(chatGptResponse.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content"));
    }
}
