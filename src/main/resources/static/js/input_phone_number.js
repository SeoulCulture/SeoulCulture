document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("phone").addEventListener("keyup", function(event) {
        input_phone_number(event.target);
    });
});

function input_phone_number(phone ) {
    if( event.keyCode != 8 ) {
        const regExp = new RegExp( /^[0-9]{2,3}-^[0-9]{3,4}-^[0-9]{4}/g );
        if( phone.value.replace( regExp, "").length != 0 ) {
            if( checkPhoneNumber( phone.value ) == true ) {
                let number = phone.value.replace( /[^0-9]/g, "" );
                let tel = "";
                let seoul = 0;
                if( number.substring( 0, 2 ).indexOf( "02" ) == 0 ) {
                    seoul = 1;
                    phone.setAttribute("maxlength", "12");
                    console.log( phone );
                } else {
                    phone.setAttribute("maxlength", "13");
                }
                if( number.length < ( 4 - seoul) ) {
                    return number;
                } else if( number.length < ( 7 - seoul ) ) {
                    tel += number.substr( 0, (3 - seoul ) );
                    tel += "-";
                    tel += number.substr( 3 - seoul );
                } else if(number.length < ( 11 - seoul ) ) {
                    tel += number.substr( 0, ( 3 - seoul ) );
                    tel += "-";
                    tel += number.substr( ( 3 - seoul ), 3 );
                    tel += "-";
                    tel += number.substr( 6 - seoul );
                } else {
                    tel += number.substr( 0, ( 3 - seoul ) );
                    tel += "-";
                    tel += number.substr( ( 3 - seoul), 4 );
                    tel += "-";
                    tel += number.substr( 7 - seoul );
                }
                phone.value = tel;
            } else {
                const regExp = new RegExp( /[^0-9|^-]*$/ );
                phone.value = phone.value.replace(regExp, "");
            }
        }
    }
}

function checkPhoneNumber( number ) {
    const regExp = new RegExp( /^[0-9|-]*$/ );
    if( regExp.test( number ) == true ) { return true; }
    else { return false; }
}