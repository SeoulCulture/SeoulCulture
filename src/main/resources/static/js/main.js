document.addEventListener("DOMContentLoaded", function() {
    showParagraph();
});
function showParagraph() {
    var paragraphs = ["paragraph1", "paragraph2", "paragraph3"];
    var index = 0;

    document.getElementById(paragraphs[index]).style.opacity = "1";

    var interval = setInterval(function() {
        // 다음 단락 표시
        index++;
        if (index >= paragraphs.length) {
            clearInterval(interval); // 마지막 단락까지 표시되면 인터벌 정지
        } else {
            document.getElementById(paragraphs[index]).style.opacity = "1";
        }
    }, 1000);
}