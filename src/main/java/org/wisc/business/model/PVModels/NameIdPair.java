package org.wisc.business.model.PVModels;

import lombok.Data;

import java.io.Serializable;

@Data
public class NameIdPair implements Serializable {
    String name;
    String id;
    public NameIdPair(String name, String id) {
        this.name = (name==null?"null":name);
        this.id = id;
    }
}
