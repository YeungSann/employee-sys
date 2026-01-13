package com.employee_sys.ui;

import com.employee_sys.bean.User;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * 従業員管理システムのログイン画面を表示するGUIクラスです。
 */
public class LoginUI extends JFrame {

    // --- フィールド定義 ---
    private JTextField usernameField;      // ユーザー名入力用
    private JPasswordField passwordField;  // パスワード入力用
    private JButton loginButton;           // ログイン実行ボタン
    private JButton registerButton;        // 新規登録画面への遷移ボタン

    // ユーザーデータを格納する静的リスト
    public static ArrayList<User> alluser = new ArrayList<>();

    // 静的初期化ブロックでサンプルユーザーを追加
    static {
        alluser.add(new User("スーパーマネージャー", "123456"));
        alluser.add(new User("市川花子", "2720832"));
        alluser.add(new User("千葉太郎", "2720000"));
    }

    /**
     * コンストラクタ
     */
    public LoginUI() {
        super("従業員管理システム - ログイン");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 250);
        this.setLocationRelativeTo(null);

        // パネルとレイアウトの設定
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // 1. タイトル
        JLabel titleLabel = new JLabel("従業員管理システム", SwingConstants.CENTER);
        titleLabel.setFont(new Font("MS ゴシック", Font.BOLD, 18));
        mainPanel.add(titleLabel);

        // 2. ユーザー名
        JPanel userRow = new JPanel(new BorderLayout());
        userRow.add(new JLabel("ユーザー名 : "), BorderLayout.WEST);
        this.usernameField = new JTextField();
        userRow.add(this.usernameField, BorderLayout.CENTER);
        mainPanel.add(userRow);

        // 3. パスワード
        JPanel passRow = new JPanel(new BorderLayout());
        passRow.add(new JLabel("パスワード : "), BorderLayout.WEST);
        this.passwordField = new JPasswordField();
        passRow.add(this.passwordField, BorderLayout.CENTER);
        mainPanel.add(passRow);

        // 4. ボタン
        JPanel btnRow = new JPanel(new FlowLayout());
        this.loginButton = new JButton("ログイン");
        this.registerButton = new JButton("新規登録");
        btnRow.add(this.loginButton);
        btnRow.add(this.registerButton);
        mainPanel.add(btnRow);

        // イベント設定
        this.setupEventListeners();

        this.add(mainPanel);

        // ウィンドウを可視化します
        this.setVisible(true);
    }

    private void setupEventListeners() {
        this.loginButton.addActionListener(e -> performLogin());
        this.registerButton.addActionListener(e -> new RegisterUI(this));
    }

    private void performLogin() {
        String inputUser = this.usernameField.getText();
        String inputPass = new String(this.passwordField.getPassword());

        if (inputUser.isEmpty() || inputPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "入力してください。", "警告", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User targetUser = null;
        for (User u : alluser) {
            if (u.getUsername().equals(inputUser)) {
                targetUser = u;
                break;
            }
        }

        if (targetUser == null || !targetUser.getPassword().equals(inputPass)) {
            JOptionPane.showMessageDialog(this, "認証に失敗しました。", "エラー", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "ログイン成功！");
            this.dispose();
            new EmployeeManagementUI().setVisible(true);
        }
    }
}