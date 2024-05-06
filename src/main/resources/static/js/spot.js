// 초성검색 설정

// 초성검색 : https://gurtn.tistory.com/212
// 초성배열
const CHO_HANGUL = [
    'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ',
    'ㄹ', 'ㅁ', 'ㅂ','ㅃ', 'ㅅ',
    'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ',
    'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ',
];

// 한글 시작 유니코드
const HANGUL_START_CHARCODE = "가".charCodeAt();

// 초성, 종성 주기
const CHO_PERIOD = Math.floor("까".charCodeAt() - "가".charCodeAt());
const JUNG_PERIOD = Math.floor("개".charCodeAt() - "가".charCodeAt());

// 한글 결합 함수
function combine(cho, jung, jong) {
    return String.fromCharCode(
        HANGUL_START_CHARCODE + cho * CHO_PERIOD + jung * JUNG_PERIOD + jong
    );
}
// 초성검색
function makeRegexByCho(search = "") {
    const regex = CHO_HANGUL.reduce(
        (acc, cho, index) =>
            acc.replace(
                new RegExp(cho, "g"),
                `[${combine(index, 0, 0)}-${combine(index + 1, 0, -1)}]`
            ),
        search
    );

    return new RegExp(`(${regex})`, "g");
}
function includeByCho(search, targetWord) {
    return makeRegexByCho(search).test(targetWord);
}

// 검색
let nowIndex = 0;
const ignoreKey = [37, 38, 39, 40]
let preMatched;
let preInput;

function searchSpots() {
    let input;
    let matches;
    if (ignoreKey.includes(event.keyCode)) {
        matches = preMatched;
        input = preInput;
    } else {
        input = document.getElementById('searchInput').value.trim();
        matches = findMatchingStrings(event, input);
        preMatched = matches;
        preInput = input;
    }

    handleUpDownIndex(event, matches);
    showSearchResult(matches, input);
}

// 매칭된 검색 결과 리턴 & 정렬알고리즘(일치율 높은 내림차순)
function findMatchingStrings(event, val) {
    const matches = [];
    const inputLength = val.length;
    spotDtos.forEach((spotDto) => {
        if (includeByCho(val, spotDto.spotName)) {
            const matchingChars = calculateMatchingChars(val, spotDto.spotName);
            const matchRate = (matchingChars / inputLength) * 100; // 일치율 계산
            matches.push({ name: spotDto.spotName, matchRate: matchRate }); // 일치율과 함께 추가
        }
    });
    matches.sort((a, b) => b.matchRate - a.matchRate);
    return matches.map(match => match.name);
}
function calculateMatchingChars(str1, str2) {
    let matchingChars = 0;
    const len = Math.min(str1.length, str2.length);
    for (let i = 0; i < len; i++) {
        if (str1[i] === str2[i]) {
            matchingChars++;
        }
    }
    return matchingChars;
}


function handleUpDownIndex(event, matches) {
    switch (event.keyCode) {
        // UP KEY
        case 38:
            nowIndex = Math.max(nowIndex - 1, 0);
            break;
        // DOWN KEY
        case 40:
            nowIndex = Math.min(nowIndex + 1, matches.length - 1);
            break;
        // ENTER KEY
        case 13:
            document.querySelector("#searchInput").value = matches[nowIndex] || "";
            // 초기화
            nowIndex = 0;
            matches.length = 0;
            break;
        // 그외 다시 초기화
        default:
            nowIndex = 0;
            break;
    }
}

const showSearchResult = (matches, input) => {
    const regex = makeRegexByCho(input);

    const searchResults = document.getElementById("searchResults");
    searchResults.innerHTML = matches
        .map(
            (label, index) => `
    <div class='${nowIndex === index ? "active" : ""}'>
      ${label.replace(regex, "<mark>$1</mark>")}
    </div>`).join("");
};