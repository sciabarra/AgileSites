---
layout: page
title: Picker
---
##### Prev: [Java Idiom](JavaIdiom.html)

Here we introduce the AgileSites template engine, the picker. Picker is very helpful in keeping the HTML in his original form and place content where is needed. It will result in code much clearer without confusing mix of html and java code.

Here there is a quick introductin to the picker. Full documentation is [here](../reference/Picker.html)

## The picker

The Picker is the template engine of AgileSites.

It is a bit different by other template engines because it works reading an html file, replacing or adding content inside and returning it. 

Since most of the work in the CMS implementation requires you extract some content from the CMS and place in specific places in the template it results in effective code with a logic much cleaner since it is not required to follow a sequential order in generating an placing the content.

The Picker works a in a way similar to the jQuery javascript library (indeed it uses the same css-based selector syntax of jQuery) but the logic is written in java instead of javascript.


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

#####  Next:  [Read Content](ReadContent.html)




