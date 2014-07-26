/**
 * Created with JetBrains PhpStorm.
 * User: marco
 * Date: 29/09/13
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
$(document).ready(function(){
    var loc = window.location.pathname;
    var domname ='/responsive.sciabarra.com/';

    $('#topnavbar').find('li').each(function() {
        $(this).toggleClass('active', domname +$(this).find('a').attr('href') == loc);
    });
});