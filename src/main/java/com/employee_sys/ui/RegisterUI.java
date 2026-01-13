package com.employee_sys.ui;

import com.employee_sys.bean.User;
import javax.swing.*;
import java.awt.*;

/**
 * 新規登録画面のクラスです。
 */
public class RegisterUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton submitButton;

    public RegisterUI(JFrame parent) {
        super("新規登録");
        this.setSize(350, 300);
        this.setLocationRelativeTo(parent); // ログイン画面の中央に配置

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // ユーザー名
        usernameField = new JTextField();
        panel.add(new JLabel("ユーザー名を入力:"));
        panel.add(usernameField);

        // パスワード
        passwordField = new JPasswordField();
        panel.add(new JLabel("パスワードを入力:"));
        panel.add(passwordField);

        // 確認用パスワード
        confirmPasswordField = new JPasswordField();
        panel.add(new JLabel("パスワード（確認）:"));
        panel.add(confirmPasswordField);

        // 登録ボタン
        submitButton = new JButton("登録実行");
        panel.add(submitButton);

        this.add(panel);

        // 登録ロジック
        submitButton.addActionListener(e -> performRegister());

        this.setVisible(true);
    }

    private void performRegister() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());

        // 1. バリデーション (入力チェック)
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "全ての項目を入力してください。");
            return;
        }

        // 2. パスワード一致チェック
        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "パスワードが一致しません。");
            return;
        }

        // 3. 重複チェック (既存のユーザー名がないか)
        for (User u : LoginUI.alluser) {
            if (u.getUsername().equals(user)) {
                JOptionPane.showMessageDialog(this, "このユーザー名は既に存在します。");
                return;
            }
        }

        // 4. 静的リストに追加 (ここでLoginUIのリストを更新)
        LoginUI.alluser.add(new User(user, pass));

        JOptionPane.showMessageDialog(this, "登録が完了しました！ログインしてください。");
        this.dispose(); // 登録画面を閉じる
    }
}