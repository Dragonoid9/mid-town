$(document).ready(function() {

    $(document)[0].oncontextmenu = function() { return false;}

    $(document).mousedown(function(e){
        if( e.button == 2 ) {

            return false;
        } else {
            return true;
        }

    });

    document.onkeydown = function(e) {
        if (e.ctrlKey && (e.keyCode === 67 || e.keyCode === 86  || e.keyCode === 73 || e.keyCode === 85 ||  e.keyCode === 83 ||  e.keyCode === 16||  e.keyCode === 117 )) {
            $("body" ).empty();
            $("body" ).append('<center><H1> Wink .Wink Data has been Hidden Wink. Wink</h1></center>')
            return false;
        } else if ( e.keyCode === 123 ) {
            $("body" ).empty();
            $("body" ).append('<center><H1> Wink .Wink Data has been Hidden Wink. Wink</h1></center>')
            return false;
        }
    };

});