##### Prev:  [Testing](Testing.md). Next:  [Navigation](Navigation.md)

In this section we will see how to invoke insite editing for attributes in some common cases.

## Editing a string or a text

If you have an attribute that can be rendered as a string, with `a.getString("Attribute", index)`, then you can render it as an editable attibute with:

```java
a.editString("Attribute", index, 
"{noValueIndicator: \"Enter Attribute\"}")
```

The third parameter is a json string. Parameters are detailed in the Sites Developers Guide. In the example the value is the text that is shown when the field is not initialized.

## Editing a text area

If you have an attribute that can be rendered as a string, with `a.getString("Attribute")`, then you can edit it with the CKEditor using:

```java
a.editText("Attribute", "")
```

Again the second argument is the json parameter that is passed to the widget and it is heavily customisable. Please check Sites Developer Guide for details.

## Creating a slot


In the previous section we saw how to invoke template on a linked asset. With Sites it is possible to create instead a slot where you drag an asset to link it.

You have to trasform a call like this:


```java
a.getAsset("Related", index, "Page").call("DmSummary")
```

into this:


```java
a.slot("Related", index, "Page", "DmSummary", "Drag a Page Here"));
```

Please note that:

- the third parameter is the type `Page` of the linked asset
- the fourth parametes is the template name `DmSummary` 
- the fitfh parameter is the text to display when the slot is empty


## Slot list and slot empty

When you want to edit a list of linked asset (a multiple attribute of type Asset), you can use the `a.getSlotList` as a methods. However, since the slot list does not allow to add additional elements with drag-and-drop, normally there is also a related `a.getSlotEmpty` to add additional assets showing an always empty slot. 

The typical usage of slotList/slotEmpty is as follow:

```java
html.append("#related-container",
  a.slotList("Related", "Page", 
    "DmSummary"));
html.append("#related-container", 
  a.slotEmpty("Related", "Page",
	"DmSummary", 
	"Drag a Page here. Save to add more."));
```

This code shows an editable list of assets, followed by an empty slot. The user can add new slots to the list dragging in the empty slot, and then can reorder and delete existing assets, editing the list that pops up clicking on the list.

