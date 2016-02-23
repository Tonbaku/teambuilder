var autocompleteOptions;

$('document').ready(function(){
	setSliders();
});

$(document).ready(function(){
		autocompleteOptions = "#{autocompleteBB.getSuggestions()}";
		if(autocompleteOptions != null){
			$( "#inputPokemon" ).autocomplete({
			  source: function( request, response ) {
			          var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
			          response( $.grep( autocompleteOptions, function( item ){
			              return matcher.test( item );
			          }) );
			      }
			}).autocomplete( "instance" )._renderItem = function( ul, item ) {
			    return $( "<li>" ).append( "<a>" + item.icon + item.label + "</a></li>" ).appendTo( ul );
			}
			
		}
	});


function getAutocompleteOptions(field){
	$("#completeButton").click();
}


function onPokemonChange(){
	$('#nameChangeButton').click();
}

function setAutocompleteSrc(src){
	$('#autocompleteField').val(src);
	$('#completeButton').click();
}

function setSliders(){
	var slider = $(".slider").slider({
	  min: 0,
	  max: 252,
	  step: 4,
	  range: "min",
	  slide: function( event, ui ) {
	   
	  },
	  change: function(event, ui) {
		$(event.target).find(".ui-slider-handle").attr("title", ui.value);
		$(event.target).parent().parent().find(".evs").val(ui.value);
	  }
	});
}