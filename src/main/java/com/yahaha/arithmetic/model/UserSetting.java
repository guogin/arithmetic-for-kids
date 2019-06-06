package com.yahaha.arithmetic.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserSetting {
    private List<AdvancedScope> advancedScopes = new ArrayList<>();
    private List<SimpleScope> simpleScopes = new ArrayList<>();
}
