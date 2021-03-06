

$(document).ready(function() {

    var remember = $.cookie('remember');
    if (remember == 'true') 
    {
        var username = $.cookie('username');
        var password = $.cookie('password');
        // autofill the fields
        $('#username').val(username);
        $('#password').val(password);
    $('#login-check').attr('checked',true);//#login-check checkbox id..
    }


$("#UserLoginForm").submit(function() {
    if ($('#login-check').is(':checked')) {
        var username = $('#username').val();
        var password = $('#password').val();

        // set cookies to expire in 14 days
        $.cookie('username', username, { expires: 14 });
        $.cookie('password', password, { expires: 14 });
        $.cookie('remember', true, { expires: 14 });                
    }
    else
    {
        // reset cookies
        $.cookie('username', null);
        $.cookie('password', null);
        $.cookie('remember', null);
    }
  });
});
