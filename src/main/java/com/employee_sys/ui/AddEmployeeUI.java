package com.employee_sys.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * 従業員情報の追加および修正を行うためのダイアログクラスです。
 */
public class AddEmployeeUI extends JDialog {
    private JTextField[] fields;
    private String[] labels = {"ID", "名前", "性別", "年齢", "部署", "役職", "給与", "電話番号", "入社年月"};
    private int targetRow = -1; // 修正モード時の対象行インデックス（-1は新規追加）

    /**
     * コンストラクタ
     * @param parent 親フレーム
     * @param model  更新対象のテーブルモデル
     * @param row    修正対象の行番号（新規追加の場合は -1 を指定）
     */
    public AddEmployeeUI(JFrame parent, DefaultTableModel model, int row) {
        // rowが-1なら「新規追加」、それ以外なら「情報修正」をタイトルに設定
        super(parent, row == -1 ? "従業員新規追加" : "従業員情報修正", true);
        this.targetRow = row;
        this.setSize(400, 550);
        this.setLocationRelativeTo(parent);
        this.setLayout(new BorderLayout());

        // 入力フォームパネルの設定（9行2列のグリッドレイアウト）
        JPanel inputPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        fields = new JTextField[9];
        for (int i = 0; i < 9; i++) {
            inputPanel.add(new JLabel(labels[i] + ":"));
            fields[i] = new JTextField();

            // 修正モードの場合、既存のデータをテキストフィールドにセット
            if (row != -1) {
                fields[i].setText(model.getValueAt(row, i).toString());
            }
            inputPanel.add(fields[i]);
        }

        // 修正モード時、ID（一意識別子）の編集を不可にする
        if (row != -1) fields[0].setEditable(false);

        // ボタン配置用パネル
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("保存");
        JButton cancelButton = new JButton("キャンセル");

        // 保存ボタンのアクションリスナー
        saveButton.addActionListener(e -> {
            try {
                // 入力値の取得と配列への格納
                Object[] rowData = new Object[]{
                        fields[0].getText(), fields[1].getText(), fields[2].getText(),
                        Integer.parseInt(fields[3].getText()), fields[4].getText(), // 入力値の取得と配列への格納
                        fields[5].getText(), Double.parseDouble(fields[6].getText()), // 数値変換チェック
                        fields[7].getText(), fields[8].getText()
                };

                if (targetRow == -1) {
                    // 新規追加モード：テーブルに行を追加
                    model.addRow(rowData);
                } else {
                    // 修正モード：対象行の各セルを更新
                    for (int i = 0; i < 9; i++) {
                        model.setValueAt(rowData[i], targetRow, i); // 修改
                    }
                }
                // ダイアログを閉じる
                this.dispose();
            } catch (Exception ex) {
                // 入力エラー（数値変換失敗など）の通知
                JOptionPane.showMessageDialog(this, "入力形式が正しくありません。");
            }
        });

        // キャンセルボタン：ダイアログを破棄
        cancelButton.addActionListener(e -> this.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // レイアウトの配置
        this.add(inputPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        // ダイアログを表示
        this.setVisible(true);
    }
}