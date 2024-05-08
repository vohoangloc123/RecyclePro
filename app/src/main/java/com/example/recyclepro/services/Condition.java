package com.example.recyclepro.services;

import com.example.recyclepro.models.ConfigCondition;

public class Condition {
    public String getConditionText(int rating, ConfigCondition configCondition) {
        String conditionText;
        switch (rating) {
            case 1:
                conditionText = configCondition.muc_1;
                break;
            case 2:
                conditionText = configCondition.muc_2;
                break;
            case 3:
                conditionText = configCondition.muc_3;
                break;
            case 4:
                conditionText = configCondition.muc_4;
                break;
            case 5:
                conditionText = configCondition.muc_5;
                break;
            default:
                conditionText = "N/A";
                break;
        }
        return conditionText;
    }
}
