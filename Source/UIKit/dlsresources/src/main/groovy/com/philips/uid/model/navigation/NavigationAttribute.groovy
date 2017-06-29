package com.philips.uid.model.navigation

class NavigationAttribute {
    def colorRange
    def tonalRange

    def componentName
    def value

    @Override
    int hashCode() {
        return componentName.hashcode()
    }

    @Override
    boolean equals(Object obj) {
       if(!(obj instanceof NavigationAttribute)) {
           return false;
       }
        return componentName == obj.componentName && tonalRange == obj.tonalRange && colorRange == colorRange
    }
}