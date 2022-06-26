function expandOrCollapse(event,expandable) {
    let ul = expandable.getElementsByTagName('ul')[0];
    ul.classList.toggle('visible');
    ul.classList.toggle('hidden');
    expandable.classList.toggle('collapsed');
    expandable.classList.toggle('expanded');
    event.stopPropagation();
}

function showOrHideMenu(event,button) {
    button.classList.toggle('selected');
    let menu = document.getElementsByClassName('menu')[0];
    menu.classList.toggle('visible');
    menu.classList.toggle('hidden');
}

function showOrHideToc(event,button) {
    button.classList.toggle('selected');
    let toc = document.getElementsByTagName('aside')[0];
    toc.classList.toggle('visible');
    toc.classList.toggle('hidden');
}


function hideTocIfVisible(event,emitter) {
    let toc = document.getElementsByTagName('aside')[0];
    if (toc.classList.contains('visible')) {
        toc.classList.toggle('visible');
        toc.classList.toggle('hidden');
    }
}

window.addEventListener('load', ()=> mermaid.initialize({ startOnLoad: true }) );



