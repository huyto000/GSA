package com.example.GemSkillAssessment.enumerted;

import java.io.Serializable;

public enum EError implements Serializable {
    SUCCESSFULLY("Successfully!"),
    CONFIRM("Those passwords are not the same!"),
    EMAIL_NOT_EXIST("Email does not exist!"),
    ACCOUNT_NOT_INVITED("Account doesn's invited!"),
    TOKEN_IS_FOUNF_VERIFY("Token verifY is wrong!"),
    OLD_PASSWORD_WRONG("Old password is worng!"),
    SUBCATEGORY_NOT_DELETE("Can't delete this Sub Category!"),
    BASEITEM_NOT_DELETE("Can't delete this Base Item!"),
    EMAILNOTSEND("Unable to email invitations to "),
    SUPPERVISORNOTFOUND("Can't search supervisor!"),
    BASEITEM_IS_EXIST("Base item 's name is already exist!"),
    ID_NOT_FOUND("Id is wrong!"),
    BASEITEM_TYPE_NOT_FOUND("Base Item's type is wrong!"),
    SUB_IS_EXIST("Sub category's name is already exist!"),
    ID_BASE_IS_MISSING("Id baseItem is missing!"),
    ID_BASEITEM_NOT_FOUND("Id baseItem is wrong!"),
    USER_NOT_FOUND("User not found!"),
    TEMPLATE_NOT_EXIST("Template for user is not exist!"),
    FORM_NOT_HAS_CATE("Form hasn's category!"),
    CATE_NOT_HAS_SUB("Category hasn's sub category!"),
    SUB_NOT_HAS_BASE("Sub category hasn's base item!"),
    UNFINISH_FORM("Unfinish at base item : "),
    BASE_NOT_EXITS("Base Item does not exist!"),
    BASE_IS_EXITS("Base Item is exist!"),
    FORM_IS_FINISH("User's form is finish!"),
    FORM_REVIEW_IS_FINISH("User's form is finish review!"),
    FORM_IS_NOT_FINISH("User's form is  unfinish!"),
    FORMISREVIEWNOTFINISH("User's form is  unfinish review!"),
    CATE_IS_NULL("Category'name is null"),
    SUB_IS_NULL("Sub category'name is null"),
    BASE_IS_NULL("Base item'name is null"),
    VALIDATE_POINT("Point is wrong!"),
    PERIODSTILLOPEN("A period opened! Can't create new period"),
    NOT_HAS_PERIOD_NBALE("Period is not exist!"),
    FORM_IS_NOT_EXIST("This form isn't exist!");

    private String label;

    private EError(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public String getStatus() {
        return this.name();
    }
}
