# Exporting Assets with csdt

The shell integrates CSDT and provides a simplified interface to invoke.

Here there are the relevant commands:

<pre>
wcs-dt
</pre>

This will list the assets in the selected sites in the built.sbt


This will list all the assets and non assets in the selected sites

<pre>
wcs-dt listcs [_SELECTION_]
wcs-dt listds [_SELECTION_]
</pre>

This will listcs or listds the entities specified as _SELECTION_ (default: all the assets and the non assets).

Check the WCS Developer Tools manual for an explanation of the available syntax.

See below for a quick reference of the selection syntax

<pre>
wcs-dt export _SELECTION_
wcs-dt import _SELECTION_
</pre>

This will import or export assets and non assets specified in the selection

# SELECTIONS

Some samples:

- @ALL_ASSETS all the assets
- @ALL_NONASSETS all the non-assets (startmenu, treetab. assets types)
- @TREETAB all the treetabs
- @STARTMENU all the start menus
- @ASSET_TYPE all the asset types
- Page all the Page (you can use any other asset type)
- XXX:123456 the entity of the given type XXX and given id (can be an asset or a non-asset)
- XXX:12345,123456 the entities of the given ids.


