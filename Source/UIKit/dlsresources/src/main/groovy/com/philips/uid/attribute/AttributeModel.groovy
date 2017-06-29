package com.philips.uid.attribute

class AttributeModel {
    String name
    String refType

    @Override
    int hashCode() {
        return name.hashCode()
    }

    @Override
    boolean equals(Object o) {
        if (!(o instanceof AttributeModel)) {
            return false
        }
        return name == o.name && refType == o.refType
    }
}