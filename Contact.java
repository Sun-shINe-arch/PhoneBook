import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

class Add extends JFrame implements ActionListener {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/Contact?user=root";
    private static final String username = "root";
    private static final String password = "Crossroad@09";
    JButton button;
    JTextField text1, text2, text3, text5, text6;
    JLabel label1, label2, label3, label5, label6;
    String imagePath;

    Add() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(410, 432);
        setLayout(null);

        JLabel cont = new JLabel(new ImageIcon("Photo.jpg"));
        cont.setBounds(0, 0, 410, 432);
        add(cont);

        JPanel dropPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagePath != null) {
                    ImageIcon icon = new ImageIcon(imagePath);
                    g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), null);
                }
            }
        };
        dropPanel.setBounds(130, 40, 140, 140);
        dropPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        dropPanel.setTransferHandler(new ImageTransferHandler());
        cont.add(dropPanel);

        label1 = new JLabel("   NAME                ");
        label1.setBounds(20, 200, 100, 30);
        cont.add(label1);
        text1 = new JTextField();
        text1.setBounds(150, 200, 200, 30);
        text1.setFont(new Font("Consolas", Font.PLAIN, 15));
        cont.add(text1);

        label2 = new JLabel("   NUMBER            ");
        label2.setBounds(20, 250, 100, 30);
        cont.add(label2);
        text2 = new JTextField();
        text2.setBounds(150, 250, 200, 30);
        text2.setFont(new Font("Consolas", Font.PLAIN, 15));
        cont.add(text2);

        label3 = new JLabel("   ADDRESS            ");
        label3.setBounds(20, 300, 100, 30);
        cont.add(label3);
        text3 = new JTextField();
        text3.setBounds(150, 300, 200, 30);
        text3.setFont(new Font("Consolas", Font.PLAIN, 15));
        cont.add(text3);

        button = new JButton("Submit");
        button.setBounds(150, 350, 100, 30);
        button.setFocusable(false);
        button.setFont(new Font("Comic Sans", Font.BOLD, 13));
        button.addActionListener(this);
        cont.add(button);

        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            String name = text1.getText();
            String address = text2.getText();
            String number = text3.getText();
            saveToDatabase(name, number, address, imagePath);
        }
    }

    private void saveToDatabase(String name, String address, String number, String imagePath) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "INSERT INTO create_contact VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            File imageFile = new File(imagePath);
            FileInputStream fis = new FileInputStream(imageFile);
            stmt.setBinaryStream(1, fis, (int) imageFile.length());
            stmt.setString(2, name);
            stmt.setString(3, address);
            stmt.setString(4, number);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                int x = JOptionPane.showConfirmDialog(null, "Data saved successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                if (x == 0) {
                    System.exit(0);
                } else {
                    System.exit(0);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while saving data.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private class ImageTransferHandler extends TransferHandler {
        @Override
        public boolean canImport(TransferSupport support) {
            return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
        }

        @Override
        public boolean importData(TransferSupport support) {
            if (!canImport(support)) {
                return false;
            }

            Transferable transferable = support.getTransferable();
            try {
                @SuppressWarnings("unchecked")
                java.util.List<File> fileList = (java.util.List<File>) transferable
                        .getTransferData(DataFlavor.javaFileListFlavor);
                if (fileList.size() != 1) {
                    JOptionPane.showMessageDialog(Add.this, "Please drop only one image.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                File file = fileList.get(0);
                if (!file.getName().toLowerCase().endsWith(".jpg")) {
                    JOptionPane.showMessageDialog(Add.this, "Please drop a JPEG image.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                imagePath = file.getAbsolutePath();
                repaint();
                return true;
            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}

class Search extends JFrame implements ActionListener {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/Contact?user=root";
    private static final String username = "root";
    private static final String password = "Crossroad@09";
    JLabel label1;
    JTextField text1;
    JButton button;
    JFrame roky;

    Search() {
        roky = new JFrame();
        roky.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        roky.setSize(410, 270);
        roky.setLayout(null);

        JLabel see = new JLabel(new ImageIcon("Find.jpg"));
        see.setBounds(0, 0, 410, 270);
        roky.add(see);

        label1 = new JLabel("   NAME   :  ");
        label1.setBounds(20, 50, 130, 30);
        see.add(label1);
        text1 = new JTextField();
        text1.setBounds(100, 50, 250, 30);
        text1.setFont(new Font("Consolas", Font.PLAIN, 15));
        see.add(text1);

        button = new JButton("Submit");
        button.setBounds(150, 120, 100, 30);
        button.setFocusable(false);
        button.setFont(new Font("Comic Sans", Font.BOLD, 13));
        button.addActionListener(this);
        see.add(button);

        roky.setResizable(false);
        roky.setLocationRelativeTo(null);
        roky.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            String name = text1.getText();
            Searching(name);
            roky.dispose();
        }
    }

    public void Searching(String name) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement state = conn.createStatement();
            String query = "SELECT * FROM create_contact WHERE Name = '" + name + "'";
            ResultSet rs = state.executeQuery(query);
            if (rs.next()) {
                byte[] photoData = rs.getBytes("Photo");
                String Name = rs.getString("Name");
                String address = rs.getString("Address");
                String number = rs.getString("Number");
                displayResult(photoData, Name, address, number);
            } else {
                System.out.println("No record found for the given name.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void displayResult(byte[] photoData, String name, String address, String number) {
        JFrame frame = new JFrame("Contact Details");
        frame.setSize(410, 350);
        frame.setLayout(null);

        JLabel book = new JLabel(new ImageIcon("List.png"));
        book.setBounds(0, 0, 410, 432);
        frame.add(book);

        Image photo = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(photoData);
            photo = ImageIO.read(bis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Image finalPhoto = photo;
        JPanel photoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (finalPhoto != null) {
                    g.drawImage(finalPhoto, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        photoPanel.setBounds(140, 25, 120, 120);
        photoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        book.add(photoPanel);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(85, 155, 100, 30);
        book.add(nameLabel);
        JTextField nameField = new JTextField(name);
        nameField.setEditable(false);
        nameField.setBounds(145, 155, 150, 30);
        book.add(nameField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(85, 205, 100, 30);
        book.add(addressLabel);
        JTextField addressField = new JTextField(address);
        addressField.setEditable(false);
        addressField.setBounds(145, 205, 150, 30);
        book.add(addressField);

        JLabel numberLabel = new JLabel("Number:");
        numberLabel.setBounds(85, 255, 100, 30);
        book.add(numberLabel);
        JTextField numberField = new JTextField(number);
        numberField.setEditable(false);
        numberField.setBounds(145, 255, 150, 30);
        book.add(numberField);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class Delete extends JFrame implements ActionListener {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/Contact?user=root";
    private static final String username = "root";
    private static final String password = "Crossroad@09";
    JLabel label1, label2;
    JTextField text1, text2;
    JButton button;
    JFrame fly;

    Delete() {
        fly = new JFrame();
        fly.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fly.setSize(410, 296);
        fly.setLayout(null);

        JLabel forget = new JLabel(new ImageIcon("Forget.jpg"));
        forget.setBounds(0, 0, 410, 296);
        fly.add(forget);

        label1 = new JLabel("   NAME     ");
        label1.setBounds(20, 50, 130, 30);
        label1.setForeground(Color.RED);
        forget.add(label1);
        text1 = new JTextField();
        text1.setBounds(100, 50, 250, 30);
        text1.setFont(new Font("Consolas", Font.PLAIN, 21));
        text1.setBorder(null);
        forget.add(text1);

        label2 = new JLabel("   NUMBER     ");
        label2.setBounds(20, 100, 130, 30);
        label2.setForeground(Color.RED);
        forget.add(label2);
        text2 = new JTextField();
        text2.setBounds(100, 100, 250, 30);
        text2.setFont(new Font("Consolas", Font.PLAIN, 21));
        text2.setBorder(null);
        forget.add(text2);

        button = new JButton("Submit");
        button.setBounds(150, 170, 100, 30);
        button.setFocusable(false);
        button.setFont(new Font("Comic Sans", Font.BOLD, 13));
        button.addActionListener(this);
        forget.add(button);

        fly.setResizable(false);
        fly.setLocationRelativeTo(null);
        fly.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            String Name = text1.getText();
            String Number = text2.getText();
            delete_Data(Name, Number);
            fly.dispose();
        }
    }

    public void success() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(430, 130);
        frame.setLayout(null);

        JLabel label = new JLabel("CONTACT DELETED SUCCESSFULLY");
        label.setBounds(12, 35, 400, 30);
        label.setForeground(Color.RED);
        label.setFont(new Font("Lucida Handwriting", Font.PLAIN, 21));
        frame.add(label);

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void delete_Data(String name, String number) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement state = conn.createStatement();
            String query = "DELETE FROM create_contact WHERE Name = '" + name + "' AND Number = '" + number + "'";
            int rowsaffected = state.executeUpdate(query);
            if (rowsaffected > 0) {
                success();
            } else {
                System.out.println("Data not Deleted");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

public class Contact extends JFrame implements ActionListener {
    JFrame frame;
    JButton button1, button2, button3, button4;

    Contact() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(410, 432);
        frame.setLayout(null);

        JLabel back = new JLabel(new ImageIcon("front.jpg"));
        back.setBounds(0, 0, 410, 432);
        frame.add(back);

        button1 = new JButton();
        button1.addActionListener(this);
        button1.setText("   Add Contact   ");
        button1.setForeground(Color.green);
        button1.setFocusable(false);
        button1.setFont(new Font("Comic Sans", Font.BOLD, 15));
        button1.setBounds(80, 80, 250, 40);
        back.add(button1);

        button2 = new JButton();
        button2.addActionListener(this);
        button2.setText("   Search   ");
        button2.setFocusable(false);
        button2.setFont(new Font("Comic Sans", Font.BOLD, 15));
        button2.setBounds(80, 140, 250, 40);
        back.add(button2);

        button3 = new JButton();
        button3.addActionListener(this);
        button3.setText("   Edit   ");
        button3.setFocusable(false);
        button3.setFont(new Font("Comic Sans", Font.BOLD, 15));
        button3.setBounds(80, 200, 250, 40);
        back.add(button3);

        button4 = new JButton();
        button4.addActionListener(this);
        button4.setText("   Delete   ");
        button4.setForeground(Color.red);
        button4.setFocusable(false);
        button4.setFont(new Font("Comic Sans", Font.BOLD, 15));
        button4.setBounds(80, 260, 250, 40);
        back.add(button4);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Contact();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            new Add();
            frame.dispose();
        }
        if (e.getSource() == button2) {
            new Search();
            frame.dispose();
        }
        if (e.getSource() == button3) {

            frame.dispose();
        }
        if (e.getSource() == button4) {
            new Delete();
            frame.dispose();
        }
    }
}