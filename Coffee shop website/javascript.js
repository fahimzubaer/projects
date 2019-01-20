

//Javascript funtion to show current date and time
$(document).ready(function()
{
	var date = 'Today is: ' +  new Date($.now());
	$('#date').html(date);
});




//jquery used to highlight table cell
$(document).ready(function()
{
$('.hlight td').hover(function() {
    $(this).addClass('hover');
}, function() {
    $(this).removeClass('hover');
});
});

//count the number of characters left
$(document).ready(function()
{

    var text_max = 64;
    $('#textarea_feedback').html('Message limit of 64 characters');

    $('#textarea').keyup(function() {
        var text_length = $('#textarea').val().length;
        var text_remaining = text_max - text_length;

        if (text_remaining == 0)
            document.getElementById("textarea_feedback").style.color = "red";
        else
        	document.getElementById("textarea_feedback").style.color = "green";


        $('#textarea_feedback').html(text_remaining + ' of characters left');
    })

    });
