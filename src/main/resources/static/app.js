var socket = new SockJS('/websocket');
var stompClient = Stomp.over(socket);
const deleteButton = document.getElementById('deleteButton');
const username = document.getElementById("username").value;
const notificationLoad = document.getElementById('notificationLoad');
const groupIdJson = document.getElementById('groupId');
const calendarEl = document.getElementById('calendar');
let currentUserId = parseInt(document.getElementById('currentUserId').value, 10);
let id = 0;
let userRole = '';
let calendar;
var filesData;
let groupId = groupIdJson.value;
var selectable = true;
var droppable = true;
userRole = document.getElementById('mode').value;
if(userRole !== 'ROLE_ADMIN'){
    selectable = false;
    droppable = false;
}

console.log("Создаеться календарь...");
calendar = new FullCalendar.Calendar(calendarEl, {
    themeSystem:'bootstrap5',
    selectable: selectable,

    headerToolbar: {
        left: 'prev today',
        center: 'title',
        right: 'next'
    },
    dayMaxEventRows: true, // for all non-TimeGrid views
    locale: 'uk',
    droppable: droppable,
    // Дополнительные настройки:
    eventStartEditable: true, // Разрешает изменение начальной даты события
    expandRows:false,
    showNonCurrentDates: false,
    fixedWeekCount:false,
    navLinks: true,
    nowIndicator: false, // Включаем отображение текущего дня
    eventTextColor: 'white', // Цвет текста события

    select: function(info) {
        const startDate = info.start;
        const endDate = info.end;
        const model = document.getElementById('eventModalNew');

        // Преобразование дат в строки с учетом местного часового пояса
        const startDateFormatted = startDate.getFullYear() + '-' +
            ('0' + (startDate.getMonth() + 1)).slice(-2) + '-' +
            ('0' + startDate.getDate()).slice(-2) + 'T' +
            ('0' + startDate.getHours()).slice(-2) + ':' +
            ('0' + startDate.getMinutes()).slice(-2);

        const endDateFormatted = endDate.getFullYear() + '-' +
            ('0' + (endDate.getMonth() + 1)).slice(-2) + '-' +
            ('0' + endDate.getDate()).slice(-2) + 'T' +
            ('0' + endDate.getHours()).slice(-2) + ':' +
            ('0' + endDate.getMinutes()).slice(-2);

        // Задание значений в input элементах модального окна
        const dateOfStartInput = model.querySelector('#dateOfStart');
        const dateOfEndInput = model.querySelector('#dateOfEnd');
        dateOfStartInput.value = startDateFormatted;
        dateOfEndInput.value = endDateFormatted;

        // Здесь вы можете вызвать модальное окно для создания события
        // или как-либо иначе обработать создание события
        $(model).modal('show');
        console.log('Select event:', startDate, endDate);
    },

    eventClick: function (info) {
        notificationLoad.style.display = 'block';
        notificationLoad.classList.add('show');
        click(info.event, true);
    },
    eventDrop: function(info) {
        if(userRole === 'ROLE_ADMIN') {
            var newStartDate = info.event.start.toLocaleString();
            var newEndDate = info.event.end.toLocaleString();
            id = info.event.id;
            console.log(info.event);
            alert("Задача '" + info.event.title + "' \n" + "Дати задачі будуть змінені на:\nДата старту: " + newStartDate + "\nДата кінця: " + newEndDate);

            if (!confirm("Ви впевнені в цій зміні?")) {
                info.revert();
            } else {
                const userId = click(info.event, false);
                console.log("userId" + userId);
                editFunction(id, true, userId,);
            }
        } else {
            alert("Ви не можете змінювати задачі!");
            info.revert();
        }
    }


});
const editButton = document.getElementById('editButton');
editButton.addEventListener('click', () => {
    editFunction(id,false);
});
filesData = {
    filesCompleted: [],
    filesCreate: []
};

var dropZoneCompeted = document.getElementById('drop-zone-completed');
var dropZoneCreate = document.getElementById('drop-zone-create');



dropZoneCompeted.addEventListener('dragover', function (e) {
    dragover(e);
});

dropZoneCompeted.addEventListener('click', function () {
    clickFiles('selected-files-info', 'filesCompleted');
});

dropZoneCompeted.addEventListener('drop', function (e) {
    drop(e, 'selected-files-info', 'filesCompleted');
});
dropZoneCompeted.addEventListener('dragleave', function (e) {
    dragleave(e);
});

if(dropZoneCreate != null) {

    dropZoneCreate.addEventListener('dragover', function (e) {
        dragover(e);
    });

    dropZoneCreate.addEventListener('click', function () {
        clickFiles('selected-files-create-info', 'filesCreate');
    });

    dropZoneCreate.addEventListener('dragleave', function (e) {
        dragleave(e);
    });

    dropZoneCreate.addEventListener('drop', function (e) {
        drop(e, 'selected-files-create-info', 'filesCreate');
    });
}


function drop(e, divName, filesKey) {
    e.preventDefault();
    dropZoneCompeted.classList.remove('drag-over');
    if(dropZoneCreate != null) {
        dropZoneCreate.classList.remove('drag-over');
    }
    var newSelectedFiles = Array.from(e.dataTransfer.files);
    filesData[filesKey] = filesData[filesKey].concat(newSelectedFiles);
    handleDroppedFiles(filesData[filesKey], divName);
}

function clickFiles(divName, filesKey) {
    var fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.multiple = true;
    fileInput.style.display = 'none';

    fileInput.addEventListener('change', function () {
        var newSelectedFiles = Array.from(fileInput.files);
        filesData[filesKey] = filesData[filesKey].concat(newSelectedFiles);
        handleDroppedFiles(filesData[filesKey], divName);
    });

    document.body.appendChild(fileInput);
    fileInput.click();
    document.body.removeChild(fileInput);
}

function handleDroppedFiles(files, divName) {
    console.log(files);
    const selectedfiles = document.getElementById(divName);
    selectedfiles.innerText = "";
    for (var i = 0; i < files.length; i++) {
        var file = files[i];
        var fileInfo = document.createElement('p');
        var removeButton = document.createElement('span');
        removeButton.textContent = '✖️';
        removeButton.style.cursor = 'pointer';
        removeButton.style.marginRight = '10px';
        if(divName==='selected-files-create-info'){
            removeButton.addEventListener('click', createRemoveHandlerForCreate(file, fileInfo));
        } else {
            removeButton.addEventListener('click', createRemoveHandler(file, fileInfo));
        }

        var fileNameText = document.createElement('span');
        fileNameText.textContent = file.name;
        fileInfo.appendChild(removeButton);
        fileInfo.appendChild(fileNameText);
        selectedfiles.appendChild(fileInfo);
    }
}

function dragover(e) {
    e.preventDefault();
    dropZoneCompeted.classList.add('drag-over');
    if(dropZoneCreate !=null) {
        dropZoneCreate.classList.add('drag-over');
    }
}

function dragleave(e) {
    e.preventDefault();
    dropZoneCompeted.classList.remove('drag-over');
    if(dropZoneCreate !=null) {
        dropZoneCreate.classList.remove('drag-over');
    }
}

function createRemoveHandlerForCreate(file, fileInfo) {
    return function () {
        // Ваш код для удаления файла
        console.log('Remove file:', file);
        // Удаление элемента fileInfo из DOM
        fileInfo.parentNode.removeChild(fileInfo);
        // Удаление файла из массива
        var index = filesData.filesCreate.indexOf(file);
        if (index !== -1) {
            filesData.filesCreate.splice(index, 1);
        }
        // Обновление данных
        handleDroppedFiles(filesData.filesCreate, 'selected-files-create-info');
    };
}
function createRemoveHandler(file, fileInfo) {
    return function () {
        // Ваш код для удаления файла
        console.log('Remove file:', file);
        // Удаление элемента fileInfo из DOM
        fileInfo.parentNode.removeChild(fileInfo);
        // Удаление файла из массива
        var index = filesData.filesCompleted.indexOf(file);
        if (index !== -1) {
            filesData.filesCompleted.splice(index, 1);
        }
        // Обновление данных
        handleDroppedFiles(filesData.filesCompleted, 'selected-files-info');
    };
}


const notificationCompeted = document.getElementById('notificationCompeted');
document.getElementById('completedButton').addEventListener('click', function () {
    const formData = new FormData();
    // Перебираем массив files и добавляем каждый файл к formData
    for (var i = 0; i < filesData.filesCompleted.length; i++) {
        formData.append('files', filesData.filesCompleted[i]);
    }
    formData.append("type_file", "USER_FILE");

    console.log(filesData.filesCompleted)
    // Выполняем запрос с использованием Fetch API
    fetch(`/tasks/${id}`, {
        method: 'PATCH',
        body: formData, // Отправляем FormData, содержащий файл
    })
        .then(response => {
            response.json();
            stompClient.send(`/app/completeTask/${id}/${groupId}`, {}, '');
            fetch(`/files/task/${id}/set_files`, {
                method: 'POST',
                body: formData
            }).then(() => {
                fetch(`/email/completed_task/${id}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json' // Устанавливаем заголовок Content-Type
                    },
                    body: ''
                }).then(r  => console.log(r));
            });
        })
        .then(()=>{
            notificationCompeted.style.display = 'block';
            notificationCompeted.classList.add('show');
            setTimeout(() => {
                notificationCompeted.style.display = 'none';
                notificationCompeted.classList.remove('show');
            }, 4000);
        })
        .catch(error => {
            // Обработка ошибки
            console.error('Error updating task:', error);
        });

    // Предотвращаем отправку формы по умолчанию
    e.preventDefault();
});

const notification = document.getElementById('notificationDelete');

deleteButton.addEventListener('click', async function () {
    // Проверка, не был ли уже запущен процесс удаления

    // Отправка DELETE-запроса
    fetch(`/tasks/${id}`, {
        method: 'DELETE',
    })
        .then(() => {
            stompClient.send(`/app/deleteTask/${id}/${groupId}`, {}, '');
            // Показываем уведомление плавно
            notification.style.display = 'block';
            notification.classList.add('show'); // Добавляем класс для анимации
            setTimeout(() => {
                // Скрываем уведомление через 2 секунды
                notification.style.display='none'
                notification.classList.remove('show'); // Удаляем класс для анимации
            }, 4000);
        })
        .catch(error => {
            console.error('Error deleting task:', error);
        });

});


document.addEventListener('DOMContentLoaded', function() {
    stompClient.connect({}, function (frame) {
        stompClient.subscribe(`/topic/newTask/${groupId}`, function (message) {
            const task = JSON.parse(message.body);

            console.log('Adding event:', task.id, task.userId, task.status, task.title, task.description, task.dateOfStart, task.dateOfEnd);
            calendar.addEvent({
                id: task.id,
                title: task.title,
                allDay: task.allDay,
                status: task.status,
                userId: task.userId, // Добавляем userId в объект события
                start: task.dateOfStart,
                end: task.dateOfEnd,
                display: 'block',
                description: task.description,
                backgroundColor: getBackgroundColorByStatus(task.status),
                borderColor: getBorderColorByStatus(task.status),
                className: className(task.userId)
            });
            calendar.setOption('eventContent', function(info) {
                const eventElement = document.createElement('div');
                eventElement.classList.add('p-2', 'text-white', 'rounded', 'shadow', 'd-flex', 'justify-content-between');

                const titleElement = document.createElement('div');
                titleElement.textContent = info.event.title;


                const additionalInfo = document.createElement('small');
                additionalInfo.classList.add('text-muted');
                additionalInfo.style.fontWeight = 'bold';
                additionalInfo.textContent = getStatus(info.event.extendedProps.status);

                eventElement.appendChild(titleElement);
                eventElement.appendChild(additionalInfo);

                return {
                    domNodes: [eventElement]
                };
            });
            calendar.render();
        });
        stompClient.subscribe(`/topic/deleteTask/${groupId}`, function (message) {
            console.log(message);
            let deletedTaskId = JSON.parse(message.body);
            // Обработка успешного ответа от сервера
            console.log('Task deleted successfully');
            const event = calendar.getEventById(deletedTaskId);
            if (event) {
                event.remove();
            }

            calendar.render();
        });

        stompClient.subscribe(`/topic/completeTask/${groupId}`, function (message) {
            console.log(message);
            let completedTaskId = JSON.parse(message.body);
            const event = calendar.getEventById(completedTaskId);

            console.log(event);
            if (event) {
                event.setProp('backgroundColor', getBackgroundColorByStatus('DONE'));
                event.setProp('borderColor', getBorderColorByStatus('DONE'));
            }
            calendar.render();
        });
        stompClient.subscribe(`/topic/updateTask/${groupId}`, function (message) {
            console.log(message);
            const taskDto = JSON.parse(message.body);
            const eventOnCalendar = calendar.getEventById(taskDto.id);
            eventOnCalendar.remove();
            calendar.addEvent({
                id: taskDto.id,
                title: taskDto.title,
                allDay: true,
                status: taskDto.status,
                userId: taskDto.userId, // Добавляем userId в объект события
                start: taskDto.dateOfStart,
                end: taskDto.dateOfEnd,
                display: 'block',
                description: taskDto.description,
                backgroundColor: getBackgroundColorByStatus(taskDto.status),
                borderColor: getBorderColorByStatus(taskDto.status),
                className: className(taskDto.userId)
            });
            const eventModal = document.getElementById('eventModal');
            $(eventModal).modal('hide');
            calendar.render();
        });


    });
});

// Ваш обработчик нажатия на кнопку изменения

console.log("Group id" + groupId);
// Загрузка задач из базы данных
fetch(`/tasks/groups/${groupId}`)
    .then(response => response.json())
    .then(tasks => {
        tasks.forEach(task => {

            console.log('Adding event:', task.id, task.userId, task.status, task.title, task.description, task.dateOfStart, task.dateOfEnd);
            calendar.addEvent({
                id: task.id,
                title: task.title,
                allDay: task.allDay,
                status: task.status,
                userId: task.userId, // Добавляем userId в объект события
                start: task.dateOfStart,
                end: task.dateOfEnd,
                display: 'block',
                description: task.description,
                backgroundColor: getBackgroundColorByStatus(task.status),
                borderColor: getBorderColorByStatus(task.status),
                className: className(task.userId)
            });
        });

        calendar.setOption('eventContent', function(info) {
            const eventElement = document.createElement('div');
            eventElement.classList.add('p-2', 'text-white', 'rounded', 'shadow', 'd-flex', 'justify-content-between');

            const titleElement = document.createElement('div');
            titleElement.textContent = info.event.title;


            const additionalInfo = document.createElement('small');
            additionalInfo.classList.add('text-muted');
            additionalInfo.style.fontWeight = 'bold';
            additionalInfo.textContent = getStatus(info.event.extendedProps.status);

            eventElement.appendChild(titleElement);
            eventElement.appendChild(additionalInfo);

            return {
                domNodes: [eventElement]
            };
        });
        calendar.render();
    })
    .catch(error => {
        console.error('Error fetching tasks:', error);
    });

document.addEventListener("DOMContentLoaded", function () {
    document.getElementById('groupId').remove();
    document.getElementById('username').remove();
    document.getElementById('mode').remove();
    document.getElementById('currentUserId').remove();
});

document.getElementById('createTaskForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const title = document.getElementById('title').value;
    const description = document.getElementById('description').value;
    const dateOfStartInput = document.getElementById('dateOfStart');
    const dateOfEndInput = document.getElementById('dateOfEnd');
    const notificationCreate = document.getElementById('notificationCreate');


    const formData = new FormData();
    // Перебираем массив files и добавляем каждый файл к formData
    for (var i = 0; i < filesData.filesCreate.length; i++) {
        formData.append('files', filesData.filesCreate[i]);
    }
    formData.append("type_file", "OWNER_FILE");
    console.log(filesData.filesCreate)
    console.log(dateOfEndInput.value);
    const dateOfStart = new Date(dateOfStartInput.value).toISOString(); // Преобразование в стандартный ISO8601 формат
    const dateOfEnd = new Date(dateOfEndInput.value).toISOString();
    let userId = document.getElementById("selectedUserId").value;


    const taskData = {
        title: title,
        description: description,
        dateOfStart: dateOfStart,
        dateOfEnd: dateOfEnd,
        groupId: groupId,
        userId: userId,
        creatorEmail: username
    };

    fetch('/tasks', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(taskData)
    })
        .then(response => {
            if(response.ok){
                response.json().then(task=>{
                    stompClient.send(`/app/addTask/${groupId}`, {}, JSON.stringify(task));

                    fetch(`/files/task/${task.id}/set_files`, {
                        method: 'POST',
                        body: formData
                    }).then(response1 => response1.json())
                        .then(() => {
                            fetch(`/email/create_task/${task.id}`, {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json' // Устанавливаем заголовок Content-Type
                                },
                                body: ''
                            }).then(r  => console.log(r));
                        });
                });
            } else {
                response.text().then( error => {
                    const notificationChange = document.getElementById('notificationErrorTaskCreate');
                    const er = document.getElementById('errorTask');
                    er.innerText=error;
                    notificationChange.style.display = 'block';
                    notificationChange.classList.add('show'); // Добавляем класс для анимации

                    setTimeout(() => {

                        notificationChange.style.display = 'none';
                        notificationChange.classList.remove('show'); // Удаляем класс для анимации

                        localStorage.removeItem('showNotification'); // Удаляем флаг из localStorage
                    }, 8000);
                })
            }
        })

});


function getBackgroundColorByStatus(status) {
    switch (status) {
        case "CREATED":
            return "#6fb6fa"; // Синий цвет для CREATED
        case "IN_PROCESS":
            return "#f67452"; // Оранжевый цвет для IN_PROCESS
        case "DONE":
            return "#5cef5c"; // Зеленый цвет для DONE
        case "NOT_DONE":
            return "#f84d4d"; // Красный цвет для NOT_DONE
        default:
            return "#a2a2a2"; // Серый цвет по умолчанию
    }
}
function getBorderColorByStatus(status) {
    switch (status) {
        case "CREATED":
            return "#006cc0"; // Синий цвет для CREATED
        case "IN_PROCESS":
            return "#ff3400"; // Оранжевый цвет для IN_PROCESS
        case "DONE":
            return "#00d900"; // Зеленый цвет для DONE
        case "NOT_DONE":
            return "#ff0000"; // Красный цвет для NOT_DONE
        default:
            return "#a2a2a2"; // Серый цвет по умолчанию
    }
}
function getStatus(status) {
    switch (status) {
        case "CREATED":
            return "Створено"; // Синий цвет для CREATED
        case "IN_PROCESS":
            return "У процессі"; // Оранжевый цвет для IN_PROCESS
        case "DONE":
            return "Виконанно"; // Зеленый цвет для DONE
        case "NOT_DONE":
            return "Провалено"; // Красный цвет для NOT_DONE
    }
}
function className(userId){
    var className = '';
    let taskUserId = parseInt(userId, 10);

    if(taskUserId === currentUserId){
        className = 'blinking-event';
        console.log(className);
    }
    return className;
}


function editFunction(id,auto_click,userId){
    const event = calendar.getEventById(id);
    console.log(event);
    const eventTitle = document.getElementById('eventTitle');
    const eventDescription = document.getElementById('eventDescription');
    const eventDateStart = document.getElementById('eventDateStart');
    const eventDateEnd = document.getElementById('eventDateEnd');
    const editButtonApply = document.getElementById('editButtonApply');
    const editButton = document.getElementById('editButton');
    const eventUser = document.getElementById('eventUser');
    editButton.style.display='none';
    editButtonApply.style.display = 'block';
    let s;
    let s1;
    if(auto_click){
        s = event.start.toLocaleString();
        s1 = event.end.toLocaleString();
    } else {
        s = eventDateStart.textContent;
        s1 = eventDateEnd.textContent;
    }

    console.log(s);
    console.log(s1);
// Убираем запятые
    const cleanS = s.replace(",", "");
    const cleanS1 = s1.replace(",", "");

    const dateS = cleanS.split(" ");
    const dateS1 = cleanS1.split(" ");

    const dateComponentsS = dateS[0].split(".");
    const dateComponentsS1 = dateS1[0].split(".");
    let startDateFormatted = '';
    let endDateFormatted='';

    // Создаем новую дату, указывая компоненты в порядке "год, месяц, день, час, минуты"
    const startDate = new Date(
        dateComponentsS[2],
        dateComponentsS[1] - 1,
        dateComponentsS[0],
        dateS[1].split(":")[0],
        dateS[1].split(":")[1]
    );

    const endDate = new Date(
        dateComponentsS1[2],
        dateComponentsS1[1] - 1,
        dateComponentsS1[0],
        dateS1[1].split(":")[0],
        dateS1[1].split(":")[1]
    );

    if (isNaN(startDate) || isNaN(endDate)) {
        console.log("Invalid Date");
    } else {
        startDateFormatted = `${startDate.getFullYear()}-${(
            "0" + (startDate.getMonth() + 1)
        ).slice(-2)}-${("0" + startDate.getDate()).slice(-2)}T${(
            "0" + startDate.getHours()
        ).slice(-2)}:${("0" + startDate.getMinutes()).slice(-2)}`;

        endDateFormatted = `${endDate.getFullYear()}-${(
            "0" + (endDate.getMonth() + 1)
        ).slice(-2)}-${("0" + endDate.getDate()).slice(-2)}T${(
            "0" + endDate.getHours()
        ).slice(-2)}:${("0" + endDate.getMinutes()).slice(-2)}`;

        console.log(startDateFormatted);
        console.log(endDateFormatted);
    }


    // Создание input-полей и установка значений
    const inputTitle = document.createElement('input');
    inputTitle.type = 'text';
    inputTitle.value = event.title;
    inputTitle.className = 'form-control';

    const inputDescription = document.createElement('textarea');
    inputDescription.value = event.extendedProps.description;
    inputDescription.className = 'form-control';

    const inputDateStart = document.createElement('input');
    inputDateStart.type = 'datetime-local';
    inputDateStart.value = startDateFormatted;
    inputDateStart.className = 'form-control';

    const inputDateEnd = document.createElement('input');
    inputDateEnd.type = 'datetime-local';
    inputDateEnd.value = endDateFormatted;
    inputDateEnd.className = 'form-control';

    const submitButton = document.createElement('button');
    submitButton.type = 'submit';
    inputDateEnd.className = 'form-control';

    const selectUser = document.createElement('select');
    selectUser.id = 'change_user_selected';
    selectUser.classList.add('form-select');
    selectUser.setAttribute('aria-label', 'Default select example');

    const firstOption = document.createElement('option');
    firstOption.setAttribute('selected', 'true');
    firstOption.setAttribute('disabled', 'true');
    firstOption.textContent = 'Выберите пользователя';
    selectUser.textContent='';
    selectUser.appendChild(firstOption);


    console.log('Group id ' + groupId);
    // Здесь используется асинхронный запрос для получения списка пользователей с сервера
    fetch(`/groups/${groupId}/users`) // Замените на ваш эндпоинт для получения пользователей
        .then(response => response.json())
        .then(data => {
            // Получаем массив пользователей из JSON
            data.forEach(user => {
                const option = document.createElement('option');
                option.setAttribute('value', user.id);
                option.textContent = user.name + "(" + user.email + ")";
                selectUser.appendChild(option);
            });
        })
        .catch(error => console.error('Ошибка получения пользователей:', error));

    eventUser.innerHTML='';
    eventUser.appendChild(selectUser);

    const selectedUser = document.createElement('input');
    inputDateEnd.className = 'form-control';

    selectUser.addEventListener("change", function() {
        const selectedUserId = this.value;
        console.log('Выбран пользователь ' + selectedUserId);
        selectedUser.value = selectedUserId;
    });


    eventTitle.textContent = '';
    eventTitle.appendChild(inputTitle);

    eventDescription.textContent = '';
    eventDescription.appendChild(inputDescription);

    eventDateStart.textContent = '';
    eventDateStart.appendChild(inputDateStart);

    eventDateEnd.textContent = '';
    eventDateEnd.appendChild(inputDateEnd);

    document.getElementById('notificationChange');
    function editButtonHandlerApply (event) {
        event.preventDefault();
        editButton.style.display = 'block';
        editButtonApply.style.display = 'none';
        if (auto_click) {
            selectedUser.value = userId;
        }
        const taskId = id; // замените на актуальный ID задачи
        const taskData = {
            title: inputTitle.value,
            description: inputDescription.value,
            dateOfStart: new Date(inputDateStart.value).toISOString(),
            dateOfEnd: new Date(inputDateEnd.value).toISOString(),
            userId: selectedUser.value,
            creatorEmail: username
        };


        fetch(`/tasks/${taskId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(taskData)
        })
            .then(response => {
                if (response.ok) {
                    console.log('Task updated:');
                    localStorage.setItem('showNotification', 'true');
                    response.json().then(taskDto => {
                        console.log(taskDto);
                        stompClient.send(`/app/updateTask/${groupId}`, {}, JSON.stringify(taskDto));
                    });
                } else {
                    response.text().then(error => {
                        const notificationChange = document.getElementById('notificationErrorTaskCreate');
                        const er = document.getElementById('errorTask');
                        er.innerText = error;
                        notificationChange.style.display = 'block';
                        notificationChange.classList.add('show'); // Добавляем класс для анимации
                        const eventModal = document.getElementById('eventModal');
                        $(eventModal).modal('hide');
                        setTimeout(() => {
                            // Скрываем уведомление через 2 секунды
                            notificationChange.style.display = 'none';
                            notificationChange.classList.remove('show'); // Удаляем класс для анимации

                            localStorage.removeItem('showNotification'); // Удаляем флаг из localStorage
                        }, 8000);
                    });
                }
            });
        editButtonApply.removeEventListener("click", editButtonHandlerApply);

    }
    editButtonApply.removeEventListener("click", editButtonHandlerApply);
    // Добавляем новый обработчик события
    editButtonApply.addEventListener("click", editButtonHandlerApply);
    if(auto_click){
        console.log("event_click")
        editButtonApply.click();
    }

}


function click(event,show_modal) {
    console.log(event);
    const eventModal = document.getElementById('eventModal');
    const eventTitle = document.getElementById('eventTitle');
    const eventDescription = document.getElementById('eventDescription');
    const eventDateStart = document.getElementById('eventDateStart');
    const eventDateEnd = document.getElementById('eventDateEnd');
    const eventStatus = document.getElementById('eventStatus');
    const eventUser = document.getElementById('eventUser');
    const iconElement = document.getElementById('icon');
    const head = document.getElementById('head');
    const completedButton = document.getElementById('completedButton');
    const editButton = document.getElementById('editButton');
    const editButtonApply = document.getElementById('editButtonApply');
    const filesDivOwner = document.getElementById("files-owner");
    const filesDivUser = document.getElementById("files-user");
    const editFilesButton = document.getElementById("editFilesButton");
    const editFilesButtonCompletedChanges = document.getElementById("editFilesButtonCompletedChanges");
    var emptyFilesOwner = document.createElement("small");
    emptyFilesOwner.textContent = 'Власник не додав файлів';
    var emptyFilesUser = document.createElement("small");
    const notificationLoad = document.getElementById('notificationLoad');

    editButton.style.display='block';
    editButtonApply.style.display='none';
    // Добавляем класс для анимации

    emptyFilesUser.textContent = 'Користувач не додав файлів';
    filesDivOwner.textContent = '';
    filesDivUser.textContent = '';

    var statusName = getStatus(event.extendedProps.status);
    id = event.id;
    const userId = event.extendedProps.userId;
    var buttons = [];
    var deletedFiles = [];


    // Загрузка информации о пользователе
    fetch(`/users/${userId}/dto`)
        .then(response => response.json())
        .then(user => {
            // Отображение информации о пользователе
            console.log(user.name);
            console.log(user.email);
            eventUser.innerHTML = `${user.name} <br> ${user.email}`;
            eventTitle.textContent = event.title;
            eventDescription.textContent = event.extendedProps.description;
            eventDateStart.textContent = `${event.start.toLocaleString()}`;
            eventDateEnd.textContent = `${event.end.toLocaleString()}`;
            eventStatus.textContent = statusName;
        })
        .then(() => {
            if (show_modal) {
                notificationLoad.style.display = 'none';
                notificationLoad.classList.remove('show');
                console.log("Показываем модальное окно задача...");
                $(eventModal).modal('show');
            }
        }).catch(error => {
        console.error('Error fetching user:', error);
    });

    if(userRole !== 'ROLE_ADMIN'){
        editButton.style.display='none';
        deleteButton.style.display='none';
    }
    if ((parseInt(currentUserId) === parseInt(userId) && statusName !=='Виконанно' && statusName !=='Провалено') || userRole === 'ROLE_ADMIN') {
        completedButton.style.display = 'block';
    } else {
        completedButton.style.display = 'none';
    }


    switch (statusName) {
        case "Створено":
            iconElement.setAttribute('name', 'save-outline');
            eventStatus.style.color = '#6fb6fa'; // Изменение цвета текста
            head.style.backgroundColor='#6fb6fa';
            eventDateEnd.style.color = '#6fb6fa';
            break;
        case "У процессі":
            iconElement.setAttribute('name', 'construct-outline');
            eventStatus.style.color = '#ff3a01';
            head.style.backgroundColor='#ff3a01';
            eventDateEnd.style.color = '#ff3a01';
            break;
        case "Виконанно":
            iconElement.setAttribute('name', 'checkmark-outline'); // Имя иконки для DONE
            eventStatus.style.color = '#65f346';
            head.style.backgroundColor='#65f346';
            eventDateEnd.style.color = '#65f346';
            break;
        case "Провалено":
            iconElement.setAttribute('name', 'close-circle-outline'); // Имя иконки для NOT_DONE
            eventStatus.style.color = '#e1003b';
            head.style.backgroundColor='#e1003b';
            eventDateEnd.style.color = '#e1003b';
            break;
    }


    fetch(`/files/task/${id}/get_files`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(files => {
            console.log(files);
            // Предполагаем, что вам приходит массив файлов
            files.forEach(file => {
                // Создаем элемент для отображения информации о файле
                var fileInfo = document.createElement('div'); // Используем div как контейнер
                fileInfo.style.display='flex';

                // Создаем кнопку с иконкой для скачивания файла
                var downloadButton = document.createElement('button');
                downloadButton.className = 'btn rounded-pill px-2 my-btn'; // Добавляем классы кнопке

                var deleteButton = document.createElement('button');
                deleteButton.className = 'btn rounded-pill px-2 my-btn'; // Добавляем классы кнопке

                // Создаем и добавляем иконку
                var downloadIcon = document.createElement('ion-icon');
                downloadIcon.setAttribute('name', 'download-outline');
                downloadIcon.style.fontSize = '1.5rem';
                downloadIcon.style.verticalAlign = 'top';
                downloadButton.appendChild(downloadIcon);

                var deleteIcon = document.createElement('ion-icon');
                deleteIcon.setAttribute('name', 'trash-outline');
                deleteIcon.style.fontSize = '1.5rem';
                deleteIcon.style.verticalAlign = 'top';
                deleteButton.appendChild(deleteIcon);
                deleteButton.style.display='none';

                deleteButton.addEventListener("click",function (e){
                    fileInfo.remove();
                    deleteFile(file.id);
                    console.log(deletedFiles);
                });
                buttons.push(deleteButton);

                // Создаем текстовый узел с именем файла
                var fileNameTextNode = document.createElement("p");
                fileNameTextNode.textContent=file.name;

                // Добавляем обработчик события клика для кнопки
                downloadButton.addEventListener('click', function() {
                    downloadFile(file.id, file.name);
                });
                fileInfo.appendChild(fileNameTextNode);

                // Добавляем кнопку и текст в контейнер
                fileInfo.appendChild(downloadButton);
                fileInfo.appendChild(deleteButton);
                if(file.type==='OWNER_FILE'){
                    filesDivOwner.appendChild(fileInfo);
                } else if(file.type==='USER_FILE'){
                    filesDivUser.appendChild(fileInfo);
                }
            });
            if(filesDivOwner.textContent===''){
                filesDivOwner.appendChild(emptyFilesOwner);
            }
            if(filesDivUser.textContent===''){
                filesDivUser.appendChild(emptyFilesUser);
            }
        }).then(()=>{
        console.log("Отримання файлів завершено...");
    });

    if(editFilesButtonCompletedChanges != null) {
        editFilesButtonCompletedChanges.addEventListener("click", function () {
            fetch(`/files/delete`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(deletedFiles)
            }).then(() => {
                deletedFiles = [];
                editFilesButtonCompletedChanges.style.display = 'none';
                editFilesButton.style.display = 'block';
                const formData = new FormData();
                for (var i = 0; i < filesData.filesCompleted.length; i++) {
                    formData.append('files', filesData.filesCompleted[i]);
                }
                formData.append("type_file", "OWNER_FILE");
                fetch(`/files/task/${id}/set_files`, {
                    method: 'POST',
                    body: formData
                }).then(response=>response.json())
                    .then(files=>{
                        console.log(files);
                        if(files.length !== 0) {
                            console.log('Є файли...')
                            files.forEach(file => {
                                // Создаем элемент для отображения информации о файле
                                var fileInfo = document.createElement('div'); // Используем div как контейнер
                                fileInfo.style.display = 'flex';

                                // Создаем кнопку с иконкой для скачивания файла
                                var downloadButton = document.createElement('button');
                                downloadButton.className = 'btn rounded-pill px-2 my-btn'; // Добавляем классы кнопке

                                var deleteButton = document.createElement('button');
                                deleteButton.className = 'btn rounded-pill px-2 my-btn'; // Добавляем классы кнопке

                                // Создаем и добавляем иконку
                                var downloadIcon = document.createElement('ion-icon');
                                downloadIcon.setAttribute('name', 'download-outline');
                                downloadIcon.style.fontSize = '1.5rem';
                                downloadIcon.style.verticalAlign = 'top';
                                downloadButton.appendChild(downloadIcon);

                                var deleteIcon = document.createElement('ion-icon');
                                deleteIcon.setAttribute('name', 'trash-outline');
                                deleteIcon.style.fontSize = '1.5rem';
                                deleteIcon.style.verticalAlign = 'top';
                                deleteButton.appendChild(deleteIcon);
                                deleteButton.style.display = 'none';

                                deleteButton.addEventListener("click", function (e) {
                                    fileInfo.remove();
                                    deleteFile(file.id);
                                    console.log(deletedFiles);
                                });
                                buttons.push(deleteButton);

                                // Создаем текстовый узел с именем файла
                                var fileNameTextNode = document.createElement("p");
                                fileNameTextNode.textContent = file.name;

                                // Добавляем обработчик события клика для кнопки
                                downloadButton.addEventListener('click', function () {
                                    downloadFile(file.id, file.name);
                                });
                                fileInfo.appendChild(fileNameTextNode);

                                // Добавляем кнопку и текст в контейнер
                                fileInfo.appendChild(downloadButton);
                                fileInfo.appendChild(deleteButton);

                                filesDivOwner.appendChild(fileInfo);
                            });
                            filesDivOwner.removeChild(emptyFilesOwner);
                        }

                    });
                /*
                // Все файлы успешно удалены, теперь можно выполнять дополнительные действия
                click(calendar.getEventById(id), true);*/
            }).then(()=>{
                buttons.forEach(button => {
                    button.style.display = 'none';
                });
                document.getElementById("selected-files-info").innerText='';
                filesData.filesCompleted = [];
                document.getElementById('text').textContent='Натисніть або перетягніть файли';
                document.getElementById('drop-zone-completed').classList.remove("animate");

            });

        });


        editFilesButton.addEventListener("click", function () {
            buttons.forEach(button => {
                button.style.display = 'block';
            });
            document.getElementById('text').textContent='Додайте файли...'
            document.getElementById('drop-zone-completed').classList.add("animate");
            editFilesButton.style.display = 'none';
            editFilesButtonCompletedChanges.style.display = 'block';
        });
    }
    // Функция для скачивания файла
    function downloadFile(fileId, fileName) {
        // Создаем временную ссылку
        var link = document.createElement('a');
        link.href = `/files/download/${fileId}`; // Путь к вашему контроллеру для скачивания файла
        link.download = fileName; // Устанавливаем имя файла для скачивания

        // Добавляем ссылку на страницу и вызываем событие клика
        document.body.appendChild(link);
        link.click();

        // Удаляем ссылку из DOM
        document.body.removeChild(link);
    }
    function deleteFile(fileId) {
        deletedFiles.push(fileId);
        if(filesDivOwner.textContent===''){
            var emptyFilesOwner = document.createElement("small");
            emptyFilesOwner.textContent='Власник не додав файлів';
            filesDivOwner.appendChild(emptyFilesOwner);
        }
        if(filesDivUser.textContent===''){
            var emptyFilesUser = document.createElement("small");
            emptyFilesUser.textContent='Користувач не додав файлів';
            filesDivUser.appendChild(emptyFilesUser);
        }
    }
    return userId;
}


