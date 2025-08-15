import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

/*
 * ShoppingCart Visitor Demo — Single-file Java Swing
 * Demonstrates the Visitor design pattern for a shopping cart containing
 * different item types (Book, Electronics, Clothing). Operations such as
 * printing details, calculating total price, and computing discounts are
 * implemented as Visitor classes so new operations can be added without
 * modifying item classes (Open/Closed Principle).
 *
 * Antipattern rules applied:
 *  - Avoid putting many unrelated operations inside Item classes (use Visitor).
 *  - Don't mutate items inside visitors by default; visitors compute results
 *    and return them (pure functions). If mutation is required, document it.
 *  - Keep Item classes focused on state and accept(Visitor) only.
 *  - Avoid large switch/if chains for operations — use polymorphic Visitor.
 *
 * To compile and run:
 * javac ShoppingCart_Visitor_GUI.java * ShoppingCart_Visitor_GUI
 */

/* -------------------- Visitor Pattern Interfaces -------------------- */
interface Item {
    void accept(Visitor visitor);
    String getName();
    double getUnitPrice();
    int getQuantity();
    String getType();
}

interface Visitor {
    void visit(Book book);
    void visit(Electronics electronics);
    void visit(Clothing clothing);
}

/* -------------------- Concrete Items -------------------- */
class Book implements Item {
    private final String title; private final String author; private final double price; private final int qty;
    public Book(String title, String author, double price, int qty) { this.title = title; this.author = author; this.price = price; this.qty = qty; }
    public String getAuthor() { return author; }
    @Override public String getName() { return title; }
    @Override public double getUnitPrice() { return price; }
    @Override public int getQuantity() { return qty; }
    @Override public String getType() { return "Book"; }
    @Override public void accept(Visitor visitor) { visitor.visit(this); }
}

class Electronics implements Item {
    private final String name; private final String brand; private final double price; private final int qty;
    public Electronics(String name, String brand, double price, int qty) { this.name = name; this.brand = brand; this.price = price; this.qty = qty; }
    public String getBrand() { return brand; }
    @Override public String getName() { return name; }
    @Override public double getUnitPrice() { return price; }
    @Override public int getQuantity() { return qty; }
    @Override public String getType() { return "Electronics"; }
    @Override public void accept(Visitor visitor) { visitor.visit(this); }
}

class Clothing implements Item {
    private final String name; private final String size; private final double price; private final int qty;
    public Clothing(String name, String size, double price, int qty) { this.name = name; this.size = size; this.price = price; this.qty = qty; }
    public String getSize() { return size; }
    @Override public String getName() { return name; }
    @Override public double getUnitPrice() { return price; }
    @Override public int getQuantity() { return qty; }
    @Override public String getType() { return "Clothing"; }
    @Override public void accept(Visitor visitor) { visitor.visit(this); }
}

/* -------------------- Concrete Visitors (Operations) -------------------- */
// 1) Print details visitor — collects readable lines
class PrintDetailsVisitor implements Visitor {
    private final List<String> lines = new ArrayList<>();
    public List<String> getLines() { return lines; }
    @Override public void visit(Book book) {
        lines.add(String.format("[Book] %s by %s — Rs.%.2f x %d = Rs.%.2f", book.getName(), book.getAuthor(), book.getUnitPrice(), book.getQuantity(), book.getUnitPrice()*book.getQuantity()));
    }
    @Override public void visit(Electronics electronics) {
        lines.add(String.format("[Electronics] %s - %s — Rs.%.2f x %d = Rs.%.2f", electronics.getName(), electronics.getBrand(), electronics.getUnitPrice(), electronics.getQuantity(), electronics.getUnitPrice()*electronics.getQuantity()));
    }
    @Override public void visit(Clothing clothing) {
        lines.add(String.format("[Clothing] %s (size %s) — Rs.%.2f x %d = Rs.%.2f", clothing.getName(), clothing.getSize(), clothing.getUnitPrice(), clothing.getQuantity(), clothing.getUnitPrice()*clothing.getQuantity()));
    }
}

// 2) Total price visitor — computes total price (without discounts/tax)
class TotalPriceVisitor implements Visitor {
    private double total = 0.0;
    @Override public void visit(Book book) { total += book.getUnitPrice() * book.getQuantity(); }
    @Override public void visit(Electronics electronics) { total += electronics.getUnitPrice() * electronics.getQuantity(); }
    @Override public void visit(Clothing clothing) { total += clothing.getUnitPrice() * clothing.getQuantity(); }
    public double getTotal() { return total; }
}

// 3) Discount visitor — computes discounted total according to rules without mutating items
class DiscountVisitor implements Visitor {
    private double total = 0.0;
    // Example rules: 10% off clothing, Rs.20 off electronics orders over Rs.200, 5% off books
    @Override public void visit(Book book) {
        double subtotal = book.getUnitPrice() * book.getQuantity();
        total += subtotal * 0.95; // 5% off
    }
    @Override public void visit(Electronics electronics) {
        double subtotal = electronics.getUnitPrice() * electronics.getQuantity();
        if (subtotal > 200) subtotal -= 20; // flat discount
        total += subtotal;
    }
    @Override public void visit(Clothing clothing) {
        double subtotal = clothing.getUnitPrice() * clothing.getQuantity();
        total += subtotal * 0.90; // 10% off
    }
    public double getDiscountedTotal() { return total; }
}

/* -------------------- ShoppingCart (Client) -------------------- */
class ShoppingCart {
    private final List<Item> items = new ArrayList<>();
    public void add(Item item) { items.add(item); }
    public List<Item> items() { return new ArrayList<>(items); }
    public void clear() { items.clear(); }
    // Apply a visitor to every item
    public void applyVisitor(Visitor v) {
        for (Item it : items) it.accept(v);
    }
}

/* -------------------- GUI: E-commerce styled -------------------- */
class ShopFrame extends JFrame {
    private final ShoppingCart cart = new ShoppingCart();
    private final DefaultListModel<String> cartModel = new DefaultListModel<>();
    private final JList<String> cartList = new JList<>(cartModel);
    private final JTextArea outputArea = new JTextArea();

    // form fields
    private final JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Book", "Electronics", "Clothing"});
    private final JTextField nameField = new JTextField("Sample item", 12);
    private final JTextField extraField = new JTextField("Author/Brand/Size", 12);
    private final JTextField priceField = new JTextField("19.99", 6);
    private final JSpinner qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

    public ShopFrame() {
        setTitle("🛍️ ShopEasy — Visitor Pattern Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(new ShopHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(new EmptyBorder(12,12,12,12));

        // left: product form
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setPreferredSize(new Dimension(360, 0));
        left.setBorder(BorderFactory.createTitledBorder("Add Item to Cart"));

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel("Type:")); row.add(typeCombo);
        left.add(row);

        row = new JPanel(new FlowLayout(FlowLayout.LEFT)); row.add(new JLabel("Name:")); row.add(nameField); left.add(row);
        row = new JPanel(new FlowLayout(FlowLayout.LEFT)); row.add(new JLabel("Extra:")); row.add(extraField); left.add(row);
        row = new JPanel(new FlowLayout(FlowLayout.LEFT)); row.add(new JLabel("Price:")); row.add(priceField); row.add(new JLabel("Qty:")); row.add(qtySpinner); left.add(row);

        JButton addBtn = new JButton("Add to Cart");
        JButton clearBtn = new JButton("Clear Cart");
        left.add(Box.createRigidArea(new Dimension(0,6)));
        left.add(addBtn); left.add(Box.createRigidArea(new Dimension(0,6))); left.add(clearBtn);

        // center: cart list
        JPanel mid = new JPanel(new BorderLayout());
        mid.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));
        cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mid.add(new JScrollPane(cartList), BorderLayout.CENTER);

        // right: operations (visitors)
        JPanel right = new JPanel(); right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(BorderFactory.createTitledBorder("Operations (Visitors)"));
        JButton printBtn = new JButton("Print Details");
        JButton totalBtn = new JButton("Calculate Total");
        JButton discountBtn = new JButton("Calculate Discounted Total");
        JButton customBtn = new JButton("Apply All Visitors (debug)");
        right.add(printBtn); right.add(Box.createRigidArea(new Dimension(0,6)));
        right.add(totalBtn); right.add(Box.createRigidArea(new Dimension(0,6)));
        right.add(discountBtn); right.add(Box.createRigidArea(new Dimension(0,6)));
        right.add(customBtn);

        center.add(left, BorderLayout.WEST);
        center.add(mid, BorderLayout.CENTER);
        center.add(right, BorderLayout.EAST);

        add(center, BorderLayout.CENTER);

        // bottom: output
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane outSp = new JScrollPane(outputArea);
        outSp.setBorder(BorderFactory.createTitledBorder("Output"));
        outSp.setPreferredSize(new Dimension(0, 220));
        add(outSp, BorderLayout.SOUTH);

        // events
        addBtn.addActionListener(e -> onAddItem());
        clearBtn.addActionListener(e -> { cart.clear(); rebuildCart(); outputArea.setText(""); });

        printBtn.addActionListener(e -> doPrintDetails());
        totalBtn.addActionListener(e -> doTotal());
        discountBtn.addActionListener(e -> doDiscount());
        customBtn.addActionListener(e -> doAllVisitors());

        typeCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) updateExtraPlaceholder();
        });

        // seed with sample items
        seedSampleItems();
    }

    private void updateExtraPlaceholder() {
        String type = (String) typeCombo.getSelectedItem();
        if ("Book".equals(type)) extraField.setText("Author");
        else if ("Electronics".equals(type)) extraField.setText("Brand");
        else extraField.setText("Size");
    }

    private void onAddItem() {
        try {
            String type = (String) typeCombo.getSelectedItem();
            String name = nameField.getText().trim(); if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "Name is required"); return; }
            String extra = extraField.getText().trim(); if (extra.isEmpty()) extra = "-";
            double price = Double.parseDouble(priceField.getText().trim());
            int qty = (Integer) qtySpinner.getValue();
            Item item;
            assert type != null;
            if (type.equals("Book")) {
                item = new Book(name, extra, price, qty);
            } else if (type.equals("Electronics")) {
                item = new Electronics(name, extra, price, qty);
            } else {
                item = new Clothing(name, extra, price, qty);
            }
            cart.add(item);
            rebuildCart();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number for price");
        }
    }

    private void rebuildCart() {
        cartModel.clear();
        List<Item> items = cart.items();
        for (int i = 0; i < items.size(); i++) {
            Item it = items.get(i);
            cartModel.addElement(String.format("%02d - %s | %s | Rs.%.2f x%d", i, it.getType(), it.getName(), it.getUnitPrice(), it.getQuantity()));
        }
        cartList.setModel(cartModel);
    }

    private void doPrintDetails() {
        PrintDetailsVisitor pv = new PrintDetailsVisitor();
        cart.applyVisitor(pv);
        List<String> lines = pv.getLines();
        StringBuilder sb = new StringBuilder();
        sb.append("Cart Details:\n");
        lines.forEach(l -> sb.append(l).append("\n"));
        outputArea.setText(sb.toString());
    }

    private void doTotal() {
        TotalPriceVisitor tv = new TotalPriceVisitor();
        cart.applyVisitor(tv);
        double total = tv.getTotal();
        outputArea.setText(String.format("Total price (no discounts): Rs.%.2f", total));
    }

    private void doDiscount() {
        DiscountVisitor dv = new DiscountVisitor();
        cart.applyVisitor(dv);
        double discounted = dv.getDiscountedTotal();
        outputArea.setText(String.format("Discounted total (rules applied): Rs.%.2f", discounted));
    }

    private void doAllVisitors() {
        StringBuilder sb = new StringBuilder();
        PrintDetailsVisitor pv = new PrintDetailsVisitor(); cart.applyVisitor(pv);
        pv.getLines().forEach(l -> sb.append(l).append("\n"));
        TotalPriceVisitor tv = new TotalPriceVisitor(); cart.applyVisitor(tv);
        sb.append(String.format("\nTotal (no discounts): Rs.%.2f\n", tv.getTotal()));
        DiscountVisitor dv = new DiscountVisitor(); cart.applyVisitor(dv);
        sb.append(String.format("Discounted total: Rs.%.2f\n", dv.getDiscountedTotal()));
        outputArea.setText(sb.toString());
    }

    private void seedSampleItems() {
        cart.add(new Book("The Art of Java", "Ada Coder", 29.99, 1));
        cart.add(new Electronics("Smartphone X", "PhoneCo", 699.00, 1));
        cart.add(new Clothing("T-Shirt", "M", 19.50, 2));
        rebuildCart();
    }
}

/* -------------------- Attractive Header -------------------- */
class ShopHeader extends JPanel {
    public ShopHeader() { setPreferredSize(new Dimension(0, 96)); setLayout(new BorderLayout()); setOpaque(false); }
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth(), h = getHeight();
        GradientPaint gp = new GradientPaint(0,0,new Color(255,140,0), w, h, new Color(255,94,98));
        g2.setPaint(gp); g2.fillRect(0,0,w,h);
        g2.setColor(Color.white);
        g2.setFont(new Font("Poppins", Font.BOLD, 26));
        g2.drawString("ShopEasy — Visitor Pattern E‑Commerce Demo", 18, 36);
        g2.setFont(new Font("Inter", Font.PLAIN, 13));
        g2.drawString("Add items to cart and run operations (print, total, discounts) via Visitors.", 18, 58);
        g2.setFont(new Font("Serif", Font.BOLD, 40));
        g2.drawString("🛒", w - 84, 48);
    }
}

public class ShoppingCart_Visitor_GUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ShopFrame().setVisible(true));
    }
}