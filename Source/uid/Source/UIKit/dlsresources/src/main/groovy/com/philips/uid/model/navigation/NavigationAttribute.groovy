package com.philips.uid.model.navigation

class NavigationAttribute {
    def tonalRange
    def componentName

    //Value like @color/uid_uid_group_blue_level_5
    //We can't have exact values as it can depend on multiple reference redirection.
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
        return componentName == obj.componentName && tonalRange == obj.tonalRange
    }

    def getColorValue() {
        def colorValue = value
        def split = value.split("_")
        if(split[split.length-1].number) {
            colorValue = "?attr/uidColorLevel${split[split.length-1]}"
        }
        return colorValue
    }
}