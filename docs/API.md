# Scala API

# API

Dispatcher invokes classes implementing the ``wcs.Element`` trait.

Each Element must implement the method ``apply(e: Env)`` 

This ``x: Env`` variable is the entry point to invoke the enviroment.

## Accessing variables and lists ##

- Read a variable as a String: <br>``e("var")``

- Read a list as a sequence and then iterate it: <br>``e.list("list")``

Example:

<pre>
for(row &lt;- x.list("list")) {
   row("value")
}
</pre>

## Getting the current asset 

(TODO)

- Retrieve an asset using current c/cid: <br>``val a = x.asset``
- Retrieve the id of the current asset using c/cid:<br>``val id = x.id``
- Retrieve the id of the asset using different variables:<br>``val id = x.id("otype", "oid")``
- Create a new asset Id:<br>``Id("Page" x("otype"))``
- Retrieve an asset using different variable values:<br>``val a = x.asset(x.id("otype", "oid"))``

## Calling another Template ##

(TODO)

- Call a template with current c/cid:<br>``x.call("Body")``
- Call a template with different id value:<br>``x.call(x.id("otype","oid"), "Body")``

## Get Template Url

(TODO)

- Generate a url calling the given template (default Layout):<br>``x.url(x.id)``

## Accessing attributes

(TODO)

Given ``a: Asset = x.asset``

- Retrieve an attribute (single value):<br>``a("attribute")``
- Retrieve a group of attributes:<br>``(title, summary) = a("Title", "Summary")``
- Iterate the multiple values of a multivalued attribute:<br>

<pre>
&lt;ul>{ 
    a("children") { 
      (title, related) =>
	  &lt;li>&lt;a href={url(x("c"), related)}>{title}&lt;/a>&lt;/li>
    } 
}&lt;/ul>
</pre>
