##### Prev:  [Picker](Picker.md).

In this section we will see how to create content specific for the home page.

In particular we will add a title, an image, a text and a reference to another page.

## Creating a new attribute

Here is step by step how to create an attribute named `MyTitle`.

First go in the administrative interface, click on **New** and then click on **New Page Attribute**

![New Page Attribute](../img/snap4806.png)

Create a new Page Attribute name `MyTitle`, but use the name without the prefix in the description (so the user won't see the prefix). Select `string` as attribute type, and `single` as number of values.

![New Page Attribute ](../img/snap4829.png)

Now search for the Page Definition we created before. In the administrative interface, click on `Find` then `Find Page Definition`.

![Find Page Definition](../img/snap6558.png)

Search for the `MyHome` page definition, edit it and add as a required field the newly created attribute MyTitle.

![Add attribute](../img/snap7313.png)


### Add other attributes

Now, create an attribute editor named `MyCKEditor` using this xml file:

```xml
<PRESENTATIONOBJECT>
 <CKEDITOR/>
</PRESENTATIONOBJECT>
```

Following the same procedure as before, create the following attributes:

- **MyText**, attribute type `text`, single, attribute editor `MyCKEditor`
- **MyImage**, attribute type `blob`, single
- **MyRelated**, attribute type `asset`, multipe, for asset type `Page`, asset subtypes `Any`

then add them to the Page Definition `MyHome` (`MyText` should be mandatory, `MyImage` and `MyRelated` optional).

Finally go in the contributor interface, and edit the content.

XXX

## Rendering the attribute

Now 
For a quick introductory example a common usage is to pick a template, select the content then replace the title with the attribute title. Here `a.getString("Title")` returns the title attribute but it will be explained next. The typical Picker usage is then:

```java
// load the given template and then restrict to the element with id=body
Picker p = Picker.load("/site/template.html", "#body");
// replace the html of the title with the actual title attribute
p.replace("#title", a.getString("Title");
// return the html
return p.html();
```

Note that picker uses a fluent interface, and almost all the methods returns itself so the precedent example can be written:

```java
return Picker.load("/site/template.html", "#body")
   .replace("#title", a.getString("Title").htlm();
```



##### Next:  [Testing](Testing.md)




