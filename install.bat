@ECHO OFF
if exist ofm_sites_generic_11.1.1.8.0_disk1_1of1.zip goto wcs118
if exist ofm_sites_generic_11.1.1.6_bp1_disk1_1of1.zip goto wcs116
cls
echo Please download WebCenter Sites 11.1.1.6.1 or 11.1.1.8.0 
echo and place in this folder.
start http://www.oracle.com/technetwork/middleware/webcenter/sites/downloads/index.html
goto pause
:wcs118
jar xvf ofm_sites_generic_11.1.1.8.0_disk1_1of1.zip WebCenterSites_11.1.1.8.0/WCS_Sites/WCS_Sites.zip
cd wcs
jar xvf ..\WebCenterSites_11.1.1.8.0\WCS_Sites\WCS_Sites.zip
goto setup
:wcs116
jar xvf ofm_sites_generic_11.1.1.6_bp1_disk1_1of1.zip WebCenterSites_11.1.1.6_bp1/WCS_Sites_11.1.1.6_bp1/WCS_Sites_11.1.1.6_bp1.zip
cd wcs
jar xvf ..\WebCenterSites_11.1.1.6_bp1\WCS_Sites_11.1.1.6_bp1\WCS_Sites_11.1.1.6_bp1.zip
:setup
call setup
goto pause
:pause
pause
cls
