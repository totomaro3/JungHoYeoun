/*
$('select[data-value]').each(function(index, el) {
	const $el = $(el);

	const defaultValue = $el.attr('data-value').trim();

	if (defaultValue.length > 0) {
		$el.val(defaultValue);
	}
});
*/

$('.popup').click(function() {
	//	$('.layer').css('display','block');
	$('.layer').show();
	$('.layer-bg').show();
});

$('.close-btn').click(function() {
	//	$('.layer').css('display','none');
	$('.layer').hide();
	$('.layer-bg').hide();
});

$('.layer-bg').click(function() {
	//	$('.layer').css('display','none');
	$('.layer').hide();
	$('.layer-bg').hide();
});