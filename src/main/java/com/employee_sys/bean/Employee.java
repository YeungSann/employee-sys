package com.employee_sys.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 従業員の詳細情報を保持するBean
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private String id;          // ID
    private String name;        // 名前
    private String gender;      // 性別
    private int age;            // 年齢
    private String department;  // 部署
    private String position;    // 役職
    private double salary;      // 給与
    private String phoneNumber; // 電話番号
    private String hireDate;    // 入社年月 (String形式、またはLocalDate)

    // テーブル表示用にObject配列へ変換するメソッド
    public Object[] toArray() {
        return new Object[]{id, name, gender, age, department, position, salary, phoneNumber, hireDate};
    }

    // バリデーションの例
    public void validate() {
        assert id != null && id.startsWith("EMP") : "不正確な従業員ID形式です";
        assert age >= 18 && age <= 65 : "年齢は18歳から65歳の間である必要があります";
        assert salary >= 0 : "給与はマイナスになりません";
    }
}