##### Prev: [Java Idiom](JavaIdiom.md)

Here we introduce the AgileSites template engine, the picker. Picker is very helpful in keeping the HTML in his original form and place content where is needed. It will result in code much clearer without confusing mix of html and java code.

## The picker

The Picker is the template engine of AgileSites.

It is a bit different by other template engines because it works reading an html file, replacing or adding content inside and returning it. 

Since most of the work in the CMS implementation requires you extract some content from the CMS and place in specific places in the template it results in effective code with a logic much cleaner since it is not required to follow a sequential order in generating an placing the content.

The Picker works a in a way similar to the jQuery javascript library (indeed it uses the same css based selectors of jquery) but the logic is written in java instead of javascript.


For a quick introductory example a common usage is to pick a template, select the content then replace the title with the attribute title. Here `a.getString("Title")` returns the title attribute but it will be explained next. The typical Picker usage is then:

<pre>
// load the given template and then restrict to the element with id=body
Picker p = Picker.load("/site/template.html", "#body");
// replace the html of the title with the actual title attribute
p.replace("#title", a.getString("Title");
// return the html
return p.html();
</pre>

Note that picker uses a fluent interface, and almost all the methods returns itself so the precedent example can be written:

<pre>
return Picker.load("/site/template.html", "#body")
   .replace("#title", a.getString("Title").htlm();
</pre>

## Selectors

The actual library implementing the Picker is the [JSoup Library](http://jsoup.org) and inherits its selector syntax. All the available selectors are  described [in detail here](http://jsoup.org/apidocs/org/jsoup/select/Selector.html)

For reference a list of selector, literally copied from [here](http://jsoup.org/cookbook/extracting-data/selector-syntax) is available next.

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
-    *: all elements, e.g. *

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

## Input and output

Let's see here how you read the html resources and return them manipulated.

### Resources

Picker mostly works loading html from the resources. All the html placed in the static folder are copied to the resource folders and then finally deployed into the jar, from where they can be loaded by the Picker.

The main Picker creation method is `Picker.load("/mysite/simple.html")` that can load a complete html resource. Note that the resources must be placed under the `src/main/static` folder. The site generation wizard creates a folder for each new site and places a simple html template. Then the html below the static folder will be packaged in the jar. So the resource name must be absolute but within the jar. 

In practice the resource name is the path below the static folder, using direct slashes (no backslashes) and starting with a slash too.

### Input

Instead of loading a full resource, it is much more common to load only a snippet of a larger html file, using the `Picker.load(resource, selector)`. 

The loading method will discard everything that was outside of selected element. The selector is of course a string with a selector expression as described before.

You can even create a picker out of a simple string with `Picker.create`. However since long html and inline html makes the code not very readable his usage is not advised and should be limited to very short html snippets, and for them a simple `String.format` could be even more suitable.

### Output


The picker can return the whole html loaded after modifications (see below) with the method `outerHtml()`. Note however that is common to select a container in the initial picking, manipulate the html and then return only the inner part of the selected html. So the method `html()` returns only the inner part of the selected snipped of html.

For example if you have:

<pre>
...
&lt;div id="body">
  &lt;h1>Title&lt;/h1>...
&lt;div>
...
</pre>

You will probably do this

`Picker.load(/*...*/)./*some replacement logic*/.html()`


The result is:

<pre>
&lt;h1>New Title&lt;/h1>
...
</pre>

If you want also the containing `div`, using `outerHtml()` will return:

<pre>
&lt;div id="body">
 &lt;h1>Title&lt;/h1>
...
&lt;div>
</pre>

## Selection

The picker has the ability to restrict to a subset of the whole html in order to simplify the processing. It works like in this example:

<pre>
Picker p = Picker.load(...);
p.select("#inner-slot");
/* do here your changes in the inner slot */
p.up();
</pre>

Note however that even if you selected an inner element, the selection is temporary: the `html()` and `outerHtml()` will still return the whole loaded html (eventually restricted to a subset in loading - that is instead 29/05/2013 08:54:43 ).

## Replacements

Let's see which replacements methods are available:

<pre>
&lt;div id="title">
	&lt;h1>Hello&lt;/h1>
&lt;/div>
&lt;div id="content">
	&lt;p>How are you?&lt;/p>
&lt;/div>
&lt;div id="menu">
	&lt;ul>
		&lt;li>First&lt;/li>
		&lt;li>Second&lt;/li>
		&lt;li>Third&lt;/li>
	&lt;/ul>
&lt;/div></pre>

#####  Next:  [Read Content](ReadContent.md)




