package com.example.httprequest.ui;

import java.util.List;

public class TransactionResponse {

    /**
     * status : true
     * Test : Hello
     * data : [{"Id_Student":"025930461012-6","Password":"1234","Fname":"นิติพงษ์","Lname":"สารศรี","Year":"4","Branch":"1","Major":"1","ID_Campus":"1","Level":"1","Status":"","Money":"2000"}]
     */

    private String status;
    private String Test;
    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTest() {
        return Test;
    }

    public void setTest(String Test) {
        this.Test = Test;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * Id_Student : 025930461012-6
         * Password : 1234
         * Fname : นิติพงษ์
         * Lname : สารศรี
         * Year : 4
         * Branch : 1
         * Major : 1
         * ID_Campus : 1
         * Level : 1
         * Status :
         * Money : 2000
         */

        private String Id_Student;
        private String Password;
        private String Fname;
        private String Lname;
        private String Year;
        private String Branch;
        private String Major;
        private String ID_Campus;
        private String Level;
        private String Status;
        private String Money;

        public String getId_Student() {
            return Id_Student;
        }

        public void setId_Student(String Id_Student) {
            this.Id_Student = Id_Student;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }

        public String getFname() {
            return Fname;
        }

        public void setFname(String Fname) {
            this.Fname = Fname;
        }

        public String getLname() {
            return Lname;
        }

        public void setLname(String Lname) {
            this.Lname = Lname;
        }

        public String getYear() {
            return Year;
        }

        public void setYear(String Year) {
            this.Year = Year;
        }

        public String getBranch() {
            return Branch;
        }

        public void setBranch(String Branch) {
            this.Branch = Branch;
        }

        public String getMajor() {
            return Major;
        }

        public void setMajor(String Major) {
            this.Major = Major;
        }

        public String getID_Campus() {
            return ID_Campus;
        }

        public void setID_Campus(String ID_Campus) {
            this.ID_Campus = ID_Campus;
        }

        public String getLevel() {
            return Level;
        }

        public void setLevel(String Level) {
            this.Level = Level;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getMoney() {
            return Money;
        }

        public void setMoney(String Money) {
            this.Money = Money;
        }
    }
}
