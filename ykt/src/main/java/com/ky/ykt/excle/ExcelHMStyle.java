package com.ky.ykt.excle;

import java.io.Serializable;

public class ExcelHMStyle implements Serializable {
    private int columnWidth = 25;

    private String sheetName = "sheet1";

    private String xlsName = "export";
    //单据号
    private String documentNumber;
    //日期
    private String documentDate;
    //主管单位
    private String documentCompetent;
    //备注
    private String documentRemark;
    //审核意见
    private String documentOpinion;
    //单据名称
    private String documentBillName;
    //资金类型
    private String documentProjectType;
    //负责人
    private String documentPrincipalPerson;
    //经办人
    private String documentResponsiblePerson;
    //金额合计
    private String sumMoney;
    //金额合计大写
    private String bigSumMoney;

    public String getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(String sumMoney) {
        this.sumMoney = sumMoney;
    }

    public String getBigSumMoney() {
        return bigSumMoney;
    }

    public void setBigSumMoney(String bigSumMoney) {
        this.bigSumMoney = bigSumMoney;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentCompetent() {
        return documentCompetent;
    }

    public void setDocumentCompetent(String documentCompetent) {
        this.documentCompetent = documentCompetent;
    }

    public String getDocumentRemark() {
        return documentRemark;
    }

    public void setDocumentRemark(String documentRemark) {
        this.documentRemark = documentRemark;
    }

    public String getDocumentOpinion() {
        return documentOpinion;
    }

    public void setDocumentOpinion(String documentOpinion) {
        this.documentOpinion = documentOpinion;
    }

    public String getDocumentBillName() {
        return documentBillName;
    }

    public void setDocumentBillName(String documentBillName) {
        this.documentBillName = documentBillName;
    }

    public String getDocumentProjectType() {
        return documentProjectType;
    }

    public void setDocumentProjectType(String documentProjectType) {
        this.documentProjectType = documentProjectType;
    }

    public String getDocumentPrincipalPerson() {
        return documentPrincipalPerson;
    }

    public void setDocumentPrincipalPerson(String documentPrincipalPerson) {
        this.documentPrincipalPerson = documentPrincipalPerson;
    }

    public String getDocumentResponsiblePerson() {
        return documentResponsiblePerson;
    }

    public void setDocumentResponsiblePerson(String documentResponsiblePerson) {
        this.documentResponsiblePerson = documentResponsiblePerson;
    }

    public String getXlsName() {
        return xlsName;
    }

    public void setXlsName(String xlsName) {
        this.xlsName = xlsName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }
}
