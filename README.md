# Radon

![time](https://hackatime-badge.hackclub.com/U09C7CEA4JJ/Radon?label=Polokalap)
![Time](https://hackatime-badge.hackclub.com/U0922GMGGTU/Radon?label=JGJ52)

![Downloads](https://img.shields.io/modrinth/dt/radon-lib?color=darkgreen)

This mod is a very easily usable configuration library.

**Here's how to use it**:
- Add the Modrinth Maven repository to your project:
```groovy
repositories {
    maven {
        url = "https://api.modrinth.com/maven"
    }  
}     
```
- Add Radon to the dependencies of your mod:
```groovy
dependencies {
    modImplementation "maven.modrinth:radon-lib:${project.radon_version}"  
}
```
- Define the version of Radon in gradle.properties:
```properties
radon_version=1.0.0-beta.1
```
- Create a class which extends ConfigScreen:
```java
package your.mod;

import me.kitty.radon.api.*;

public class MyConfig extends ConfigScreen {
    @Override
    public String getScreenTitle() {
        return "your screen's title";
    }
    
    @Override
    protected void radon() {
        // you need tabs to create rows
        Tab tab = tab("tab's name");
        Tab otherTab = tab("second tab");
        // this is where we create our options
        // if you want a boolean or an enum
        // then you want to use ButtonRow
        enum myEnum {
            RADON,
            CONFIG
        }
        ButtonRow buttonRow = buttonRow(
                tab,
                key("button_row"), // key of the row: Radon will save the content of the row to the disk with this key
                "Label of the row",
                List.of("Tooltip", "shows when you", "hover the row", "with your mouse"),
                myEnum.RADON // either an option of your enum or true or false
        );
        // you sometimes want to run code, if the button's value is changed
        // it's very simple to do that:
        buttonRow.subscribe(newValue -> {
           // newValue is an object
           // but you can cast it into
           (boolean) newValue
           // or
           (myEnum) newValue
           // this will probably be made easier 
        });
        // now, comes the slider row
        // use this if you want a number
        SliderRow sliderRow = sliderRow(
                otherTab,
                key("slider_key"),
                "Label of the row",
                List.of("Tooltip again"),
                80, // initial value
                50, // minimum value
                100 // maximum value
        );
        // here subscribe gives you a long as newValue
        // you can also run code, if Radon loaded the saved value of the option
        sliderRow.onInit(() -> {
            long value = sliderRow.getValue();
        });
        // and if you need a string value, use InputRow
        InputRow inputRow = inputRow(
                tab,
                key("input"),
                "Label of the row",
                List.of("Tooltip"),
                "Placeholder",
                16 // limit how many characters it can be
        );
        // subscribe gives newValue as String here
    }
}
```
- Add your class to ``fabric.mod.json`` as an entrypoint:
```json
{
  "entrypoints": {
    "radon": ["your.mod.MyConfig"]
  }
}
```
It's that easy!