import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class StockTradingPlatform extends JFrame {
    private ArrayList<Stock> marketStocks = new ArrayList<>();
    private HashMap<String, Integer> portfolio = new HashMap<>();
    private double balance = 10000.00; // Starting virtual capital
    
    private DefaultListModel<String> marketListModel = new DefaultListModel<>();
    private JList<String> marketList;
    private JLabel balanceLabel;

    public StockTradingPlatform() {
        initializeMarket();

        setTitle("CodeAlpha Stock Trading Simulator");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top Panel: Balance
        balanceLabel = new JLabel("Trading Balance: $" + String.format("%.2f", balance));
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        balanceLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(balanceLabel, BorderLayout.NORTH);

        // Center Panel: Market List
        updateMarketDisplay();
        marketList = new JList<>(marketListModel);
        add(new JScrollPane(marketList), BorderLayout.CENTER);

        // Bottom Panel: Actions
        JPanel buttonPanel = new JPanel();
        JButton buyBtn = new JButton("Buy Stock");
        JButton portfolioBtn = new JButton("View My Portfolio");
        
        buttonPanel.add(buyBtn);
        buttonPanel.add(portfolioBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Logic: Buy Stock
        buyBtn.addActionListener(e -> {
            int index = marketList.getSelectedIndex();
            if (index != -1) {
                Stock selected = marketStocks.get(index);
                String input = JOptionPane.showInputDialog("How many shares of " + selected.getSymbol() + "?");
                
                try {
                    int quantity = Integer.parseInt(input);
                    double totalCost = quantity * selected.getCurrentPrice();
                    
                    if (balance >= totalCost) {
                        balance -= totalCost;
                        portfolio.put(selected.getSymbol(), portfolio.getOrDefault(selected.getSymbol(), 0) + quantity);
                        balanceLabel.setText("Trading Balance: $" + String.format("%.2f", balance));
                        JOptionPane.showMessageDialog(this, "Successfully bought " + quantity + " shares!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Insufficient funds!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number.");
                }
            }
        });

        // Logic: View Portfolio
        portfolioBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("--- Your Portfolio ---\n");
            portfolio.forEach((symbol, qty) -> sb.append(symbol).append(": ").append(qty).append(" shares\n"));
            if (portfolio.isEmpty()) sb.append("No stocks owned.");
            JOptionPane.showMessageDialog(this, sb.toString());
        });

        // Timer to simulate market price changes every 5 seconds
        Timer timer = new Timer(5000, e -> simulateMarket());
        timer.start();
    }

    private void initializeMarket() {
        marketStocks.add(new Stock("AAPL", "Apple Inc.", 175.00));
        marketStocks.add(new Stock("GOOGL", "Alphabet Inc.", 140.00));
        marketStocks.add(new Stock("TSLA", "Tesla Motors", 240.00));
        marketStocks.add(new Stock("AMZN", "Amazon.com", 130.00));
    }

    private void updateMarketDisplay() {
        marketListModel.clear();
        for (Stock s : marketStocks) {
            marketListModel.addElement(s.toString());
        }
    }

    private void simulateMarket() {
        Random r = new Random();
        for (Stock s : marketStocks) {
            double change = (r.nextDouble() - 0.5) * 5.0; // Random price change between -2.5 and +2.5
            s.setCurrentPrice(Math.max(10.0, s.getCurrentPrice() + change));
        }
        updateMarketDisplay();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StockTradingPlatform().setVisible(true));
    }
}