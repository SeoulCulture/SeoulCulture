
const animateCSS = (element, animation, speed_="", prefix = 'animate__') =>
    // We create a Promise and return it
    new Promise((resolve, reject) => {
        const animationName = `${prefix}${animation}`;
        const speed = `${prefix}${speed_}`;
        const nodes = document.querySelectorAll(element);

        nodes.forEach((node) => {
            node.classList.add(`${prefix}animated`, animationName);
            if (speed.length != 0)
                node.classList.add(speed);
            // When the animation ends, we clean the classes and resolve the Promise
            function handleAnimationEnd(event) {
                event.stopPropagation();
                node.classList.remove(`${prefix}animated`, animationName);
                if (speed.length != 0)
                    node.classList.remove(speed);
            }
            node.addEventListener('animationend', handleAnimationEnd, { once: true });
        });
    });


const animateAtClicking = (element, animation1, animation2, speed_="", prefix = 'animate__') =>
    // We create a Promise and return it
    new Promise((resolve, reject) => {
        const animationName1 = `${prefix}${animation1}`;
        const speed = `${prefix}${speed_}`;
        const nodes = document.querySelectorAll(element);
        nodes.forEach((node) => {
            node.classList.add(`${prefix}animated`, animationName1);
            if (speed.length != 0)
                node.classList.add(speed);
            function handleAnimationEnd(event) {
                event.stopPropagation();
                node.classList.remove(`${prefix}animated`, animationName1);
                if (speed.length != 0)
                    node.classList.remove(speed);
                animateCSS(element, animation2);
            }
            node.addEventListener('animationend', handleAnimationEnd, { once: true });
        });
    });

function animateMarkers() {
    animateCSS('.marker_place', 'bounceIn', "faster");
    animateCSS('.marker_emoji', 'bounceIn');
    animateCSS('.marker_category', 'pulse');
}
function jumpingHomeMarker() {
    animateCSS('.marker_home', 'bounce');
}

function getRandomAnimation() {
    const animations = ["rubberBand", "wobble", "flip"];
    const randomIndex = Math.floor(Math.random() * animations.length);
    return animations[randomIndex];
}