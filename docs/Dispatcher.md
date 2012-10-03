# Dispatcher #

The main idea of this framework is implementing templates and cselements in Scala.

For this purpose you have to create norml templates and CSElement that must perform only one operation:
call a class (coded in Java or Scala) executing the normal processing.

<pre>
&lt;%=wcs.boot.WCS.dispatch(ics, "classname")%&gt;
</pre>

Fopr the classname, use the following conventions:

## Conventions ##

As a convention, each Template or CSElement in a site, must have corresponding class.

- For a given site named for example ``Demo``, If the template is a ``CSElement``, named for example ``Wrapper``
<br>then the corresponding class should be ``Demo.view.CSElement.Wrapper`` (note the ``view`` after the site name)
- for a template, for example ``Body``,
<br> the rule is similar: ``Demo.Template.view.Body``
- Slashes in template name became ``__`` in class names 
- If the template is typed,  with a name starting with a slash like ``/Body``
<br>the template need to be in the packages  ``Typeless``: ``Demo.view.Typeless._Body
- Templates typed have corresponding subpackages :
<br> template ``Body`` for a ``Page`` should be ``Demo.view.Page.Body``
<br> `Summary/Short` for a Template for Page should be ``Demo.view.Template.Page.Summary_Short`` 
- If the template is subtyped, you need a subpackage. Note the name is usually also starting with the subtype
<br>Template for Page, subtype Home, named Body/Top: ``Demo.view.Template.Page.Home.Body_Top"


