'use strict';

/* jasmine specs for controllers go here */
describe('PhoneCat controllers', function() {
   
  beforeEach(module('phonecatApp'));

  describe('PhoneListCtrl', function(){
	  //console.log(phonecatApp)
	  it('add 3 phones', inject(function($controller){
	      var scope = {}
	      var ctrl = $controller('PhoneListCtrl', {$scope:scope})
		  
	      //console.log(ctrl)
	      //console.log(scope)
	     
	      expect(scope.phones.length).toBe(3)
	  }))
  })
  
});

