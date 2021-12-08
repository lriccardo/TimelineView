# TimelineView
[![](https://jitpack.io/v/lriccardo/TimelineView.svg)](https://jitpack.io/#lriccardo/TimelineView)

A customizable and easy-to-use Timeline View library for Android

![header](https://github.com/lriccardo/TimelineView/blob/main/screens/header.png)
## Setup

### 1. Add Jitpack to your root build.gradle

```
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

### 2. Add the dependency

```
dependencies {
    implementation 'com.github.lriccardo:TimelineView:main-SNAPSHOT'
}
```

## Usage

### Standalone view
```
<com.lriccardo.timelineview.TimelineView
    android:layout_width="wrap_content"
    android:layout_height="match_parent"
    app:timeline_item_type="first"
    app:indicator_radius="12dp"
    app:line_width="8dp"
    app:indicator_color="@color/teal_700"
    app:line_color="@color/teal_700" />
```
- View types
    <table>
        <th>View type</th>
        <th>Preview</th>        
        <th>View type</th>
        <th>Preview</th>
        <tr>
            <td>first</td>
            <td><img src="https://github.com/lriccardo/TimelineView/blob/main/screens/first.jpg" alt="first" width="200"/></td>
            <td>middle</td>
            <td><img src="https://github.com/lriccardo/TimelineView/blob/main/screens/middle.jpg" alt="middle" width="200"/></td>
        </tr>
        <tr>
            <td>last</td>
            <td><img src="https://github.com/lriccardo/TimelineView/blob/main/screens/last.jpg" alt="last" width="200"/></td>
            <td>spacer</td>
            <td><img src="https://github.com/lriccardo/TimelineView/blob/main/screens/spacer.jpg" alt="spacer" width="200"/></td>
        </tr>
    </table>
    
- Customization

    <table>
        <th>Attribute</th>
        <th>Accepted values</th>
        <th>Default</th>
        <tr>
            <td>app:timeline_item_type</td>
            <td>first | middle | last | spacer</td>
            <td>first</td>
        </tr>
        <tr>
            <td>app:indicator_radius</td>
            <td>Dimension</td>
            <td>12dp</td>
        </tr>
        <tr>
            <td>app:line_width</td>
            <td>Dimension</td>
            <td>indicator_radius/1.61</td>
        </tr>
        </tr>
            <tr>
            <td>app:indicator_color</td>
            <td>Color</td>
            <td>Color.RED</td>
        </tr>
        </tr>
            <tr>
            <td>app:line_color</td>
            <td>Color</td>
            <td>Color.RED</td>
        </tr>
    </table>

### RecyclerView Decorator
```
recyclerView.addItemDecoration(
    TimelineDecorator(
        indicatorRadius = 24f,
        lineWidth = 15f,
        padding = 48f,
        position = TimelineDecorator.Position.Left,
        indicatorColor = Color.RED,
        lineColor = Color.RED
    )
)
```

- Customization

    <table>
        <th>Field</th>
        <th>Accepted values</th>
        <th>Default</th>
        <tr>
            <td>indicatorRadius</td>
            <td>Float</td>
            <td>24f</td>
        </tr>
        <tr>
            <td>lineWidth</td>
            <td>Float</td>
            <td>indicatorRadius/1.61</td>
        </tr>        
        <tr>
            <td>padding</td>
            <td>Float</td>
            <td>indicatorRadius*2</td>
        </tr>
        <tr>
            <td>position</td>
            <td>Position (Left | Right)</td>
            <td>Left</td>
        </tr>
        <tr>
            <td>indicatorColor</td>
            <td>@ColorInt</td>
            <td>Color.RED</td>
        </tr>
        </tr>
            <tr>
            <td>lineColorr</td>
            <td>@ColorInt</td>
            <td>Color.RED</td>
        </tr>
    </table>
