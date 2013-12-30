
**NOTE** For version 1.0, not yet updated to 1.8

The Picker is the template engine provided by AgileSites to implement the "original html" templating features.

The actual library implementing the Picker is the [JSoup Library](http://jsoup.org) and inherits its selector syntax. All the available selectors are  described [in detail here](http://jsoup.org/apidocs/org/jsoup/select/Selector.html)

Here there is a guide to the Picker, including a reference list for the selector syntax, literally copied from [here](http://jsoup.org/cookbook/extracting-data/selector-syntax) is available next, and a guide to the available methods.

For the examples, please consider the `/sample.html` resource is the following:

```html
<div id="title">
	<h1>Hello</h1>
</div>
<div id="content">
	<p>How are you?</p>
</div>
<div id="menu">
	<ul>
		<li>First</li>
		<li>Second</li>
		<li>Third</li>
	</ul>
</div>
```

and that the selector syntax `#content` will select the part of the html with `id="content"`

## Input/Output

Picker mostly load snippets of html, parse them allowing to replace it with the content taken from the CMS and finally returns the HTML as string.

Html resources are bundled in the jar of the application. More specifically all the .html and .htm files placed under the directory `app/src/main/static` are packaged in the jar.

You need to use an absolute path inside the jar to locate resources, using direct slashes.

Here some samples of loading:

Load the html from a resource in the jar (packaged from the original placed under `app/src/main/static/mysite/template.html`)

```java
Picker.load("/mysite/resource.html")
```

Load from the same resource as before the html block identified by the selector "#content" (see below for the selectors):

```java
Picker.load("/mysite/resource.html", "#content")
```

You can create a picker using literal html if you need it (typically if it is a small piece of html):

```java
Picker.create("<h1>Title<h1>")
```

### html() and outerHtml()


If you load a snippet of html like this

```java
Picker p = Picker.load("/sample.html", "#title")
```

To get the whole block selected you use `p.outerHtml()`:

```html
<div id="title">
  <h1>Hello</h1>
</div>
```

If you instead use `p.html()` you will output only the inner part of the loaded html.

```html
<h1>Hello</h1>
```

## Selectors 

The picker uses the css selector syntax (very similar to the syntax used by the jQuery library) to locate parts of html you want to change.

You use selectors almost everywhere:

- `Picker.load(resource, selector)`: load a resource then select to the part of the html identified by the **selector**
- `Picker.select(selector)`: restrict to the part of the html identified by the **selector**
- `Picker.replace(selector, string)`: replace the part indentifed by the **selector** with the **string**

### Selector overview

-   **tagname**: find elements by tag, e.g. a
-   **ns|tag**: find elements by tag in a namespace, e.g. fb|name finds <fb:name> elements
-   **#id**: find elements by ID, e.g. #logo
-   **.class**: find elements by class name, e.g. .masthead
-   **[attribute]**: elements with attribute, e.g. [href]
-  **[^attr]**: elements with an attribute name prefix, e.g. [^data-] finds elements with HTML5 dataset attribute
-  **[attr^=value]**, **[attr$=value]**, **[attr\*=value]**: elements with attributes that start with, end with, or contain the value, e.g. [href*=/path/]

-   **[attr=value]**: elements with attribute value 
-   **[attr~=regex]**: elements with attribute values that match the regular expression; e.g. img[src~=(?i)\.(png|jpe?g)]
-    *: all elements, 

### Selector combinations

-    **el#id**: elements with ID, e.g. div#logo
-    **el.class**: elements with class, e.g. div.masthead
-    **el[attr]**: elements with attribute, e.g. a[href]
-    **parent > child**: child elements that descend directly from parent, e.g. div.content > p finds p elements; and body > * finds the direct children of the body tag
-    **siblingA + siblingB**: finds sibling B element immediately preceded by sibling A, e.g. div.head + div
-    **siblingA ~ siblingX**: finds sibling X element preceded by sibling A, e.g. h1 ~ p
-    **el, el, el**: group multiple selectors, find unique elements that match any of the selectors; e.g. div.masthead, div.logo

### Pseudo selectors

-  **:lt(n)**: find elements whose sibling index (i.e. its position in the DOM tree relative to its parent) is less than n; e.g. td:lt(3)
-  **:gt(n)**: find elements whose sibling index is greater than n; e.g. div p:gt(2)
-  **:eq(n)**: find elements whose sibling index is equal to n; e.g. form input:eq(1)
-  **:has(selector)**: find elements that contain elements matching the selector; e.g. div:has(p)
-  **:not(selector)**: find elements that do not match the selector; e.g. div:not(.logo)
-  **:contains(text)**: find elements that contain the given text. The search is case-insensitive; e.g. p:contains(jsoup)
-  **:containsOwn(text)**: find elements that directly contain the given text
-  **:matches(regex)**: find elements whose text matches the specified regular expression; e.g. div:matches((?i)login)
-  **:matchesOwn(regex)**: find elements whose own text matches the specified regular expression
    
Note that the above indexed pseudo-selectors are 0-based, that is, the first element is at index 0, the second at 1, etc.


## Replacements

Let's see which replacements methods are available with some samples


### replace

Replace is the most used method:

```java
Picker.load("/sample.html", "#content")
.replace("p", "welcome")
.outerHtml()
```

Result:

```html
<div id="content">
  <p>welcome.</p>
</div>	
```

### single, remove, before, after

You can return a single instance of a sequence of items, append, prepend and remove items.

Check those examples:

```java
Picker p = Picker.load("/sample.html", "#menu li");
p.single("li").html();
```

Result:

```html
<ul>
  <li>First</li>
</ul>
```

Code:

```java
p.after("li", "<li id=\"second\">Second</li>");	
p.before("#second", "<li>One and Half</li>");
p.remove("li:eq(1)")
p.html();
```

```html
<ul>
 <li>One and Half</li>
 <li id="second">Second</li>
</ul>
```

### Attributes

Some samples of replacement and removal of attributes:

Code:

```java
Picker.load("/sample.html", "#title")
.addClass("h1", "demo")
.attr("h1", "test", "demo")
.removeAttrs("div", "id")
.outerHtml();
```

Results:

```html
<div id="replaced">
   <h1 test="demo" class=" demo">Hello</h1>
</div>
```

### select, up

Picker lets you to **temporarily** restrict to a part of the html you are working with, with `p.select(selector)`. 

All the replacements then will happen only in the selected part. You can restrict further with other select, and you can come up to the stack of the selections with up.

**NOTE** the selections does not affect the output: `p.html()` and `p.outerHtml()` will return the whole originally loaded snippet of html.
