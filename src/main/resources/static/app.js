document.addEventListener('DOMContentLoaded', function() {
    let id = 0;
    const calendarEl = document.getElementById('calendar');
    const groupIdJson = document.getElementById('groupId');
    const userRole = document.getElementById('mode').value;
    let groupId = groupIdJson.value;
    console.log("Создаеться календарь...")
    const calendar = new FullCalendar.Calendar(calendarEl, {
        selectable: true,
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth'
        },
        locale: 'ru',

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

        eventClick: function(info) {
            const event = info.event;
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
            let currentUserID = document.getElementById('currentUserId').value;
            const editButton = document.getElementById('editButton');


            var statusName = getStatus(event.extendedProps.status);
            id = event.id;
            const userId = info.event.extendedProps.userId;

            // Загрузка информации о пользователе
            fetch(`/users/${userId}/login`)
                .then(response => response.text())
                .then(userLogin => {
                    // Отображение информации о пользователе
                    eventUser.textContent = `@${userLogin}`;
                    console.log(userLogin); // You can also log the response to see the data
                })
                .catch(error => {
                    console.error('Error fetching user:', error);
                });


            eventTitle.textContent = event.title;
            eventDescription.textContent = event.extendedProps.description;
            eventDateStart.textContent = `Начало: ${event.start.toLocaleString()}`;
            eventDateEnd.textContent = `Конец: ${event.end.toLocaleString()}`;
            eventStatus.textContent = statusName;

            console.log(userRole);
            console.log(currentUserID)
            console.log(userId)

            if(userRole !== 'ROLE_ADMIN'){
                editButton.style.display='none';
                deleteButton.style.display='none';
            }


            if ((parseInt(currentUserID) === parseInt(userId) && statusName !=='Выполнено' && statusName !=='Не выполнено') || userRole === 'ROLE_ADMIN') {
                completedButton.style.display = 'block';
            } else {
                completedButton.style.display = 'none';
            }


            switch (statusName) {
                case "Создан":
                    iconElement.setAttribute('name', 'save-outline');
                    eventStatus.style.color = '#6fb6fa'; // Изменение цвета текста
                    head.style.backgroundColor='#6fb6fa';
                    eventDateEnd.style.color = '#6fb6fa';
                    break;
                case "В процессе":
                    iconElement.setAttribute('name', 'construct-outline');
                    eventStatus.style.color = '#ff3a01';
                    head.style.backgroundColor='#ff3a01';
                    eventDateEnd.style.color = '#ff3a01';
                    break;
                case "Выполнено":
                    iconElement.setAttribute('name', 'checkmark-outline'); // Имя иконки для DONE
                    eventStatus.style.color = '#65f346';
                    head.style.backgroundColor='#65f346';
                    eventDateEnd.style.color = '#65f346';
                    break;
                case "Не выполнено":
                    iconElement.setAttribute('name', 'close-circle-outline'); // Имя иконки для NOT_DONE
                    eventStatus.style.color = '#e1003b';
                    head.style.backgroundColor='#e1003b';
                    eventDateEnd.style.color = '#e1003b';
                    break;
            }
            $(eventModal).modal('show');
        },
        eventDidMount: function(info) {
            // Добавление дополнительной информации о задаче при наведении
            const tooltip = new bootstrap.Tooltip(info.el, {
                title: info.event.extendedProps.description,
                placement: 'top',
                container: 'body',
                trigger: 'hover',
                delay: { show: 500, hide: 0 }
            });
        },

    });
    const editButton = document.getElementById('editButton');
    editButton.addEventListener('click', async function (){
        const eventTitle = document.getElementById('eventTitle');
        const eventDescription = document.getElementById('eventDescription');
        const eventDateStart = document.getElementById('eventDateStart');
        const eventDateEnd = document.getElementById('eventDateEnd');
        const changeButton = document.getElementById('editButton');
        const eventUser = document.getElementById('eventUser');



        // Преобразование даты и времени в формат datetime-local
        const startDate = new Date(eventDateStart.textContent);
        const endDate = new Date(eventDateEnd.textContent);

        const startDateFormatted = `${startDate.getFullYear()}-${('0' + (startDate.getMonth() + 1)).slice(-2)}-${('0' + startDate.getDate()).slice(-2)}T${('0' + startDate.getHours()).slice(-2)}:${('0' + startDate.getMinutes()).slice(-2)}`;

        const endDateFormatted = `${endDate.getFullYear()}-${('0' + (endDate.getMonth() + 1)).slice(-2)}-${('0' + endDate.getDate()).slice(-2)}T${('0' + endDate.getHours()).slice(-2)}:${('0' + endDate.getMinutes()).slice(-2)}`;

        // Создание input-полей и установка значений
        const inputTitle = document.createElement('input');
        inputTitle.type = 'text';
        inputTitle.value = eventTitle.textContent;
        inputTitle.className = 'form-control';

        const inputDescription = document.createElement('textarea');
        inputDescription.value = eventDescription.textContent;
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

        const idGroup = document.getElementById('groupId').value;
        console.log('Group id ' + idGroup);
        // Здесь используется асинхронный запрос для получения списка пользователей с сервера
        fetch(`/users/groups/${idGroup}`) // Замените на ваш эндпоинт для получения пользователей
            .then(response => response.json())
            .then(data => {
                // Получаем массив пользователей из JSON
                data.forEach(user => {
                    const option = document.createElement('option');
                    option.setAttribute('value', user.id);
                    option.textContent = user.login;
                    selectUser.appendChild(option);
                });
            })
            .catch(error => console.error('Ошибка получения пользователей:', error));

        eventUser.textContent='';
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

        const notificationChange = document.getElementById('notificationChange');
        changeButton.addEventListener("click", function (event) {
            event.preventDefault();

            const taskId = id; // замените на актуальный ID задачи
            const taskData = {
                title: inputTitle.value,
                description: inputDescription.value,
                dateOfStart: new Date(inputDateStart.value).toISOString(),
                dateOfEnd: new Date(inputDateEnd.value).toISOString(),
                userId: selectedUser.value
            };

            fetch(`/tasks/${taskId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(taskData)
            })
                .then(response => response.json())
                .then(task => {
                    // Обработка успешного ответа от сервера
                    console.log('Task updated:', task);
                    localStorage.setItem('showNotification', 'true');
                    window.location.reload();

                })
                .catch(error => {
                    // Обработка ошибки
                    console.error('Error updating task:', error);
                    // Записываем флаг в localStorage перед перезагрузкой страницы
                    localStorage.setItem('showNotification', 'true');
                    window.location.reload();

                });
        });
    });

    const notificationCompeted = document.getElementById('notificationCompeted');
    document.getElementById('completedButton').addEventListener('click',function () {
        fetch(`/tasks/${id}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                // Обработка успешного ответа от сервера
                console.log('Task updated:', data);
                // Найти событие по айди
                const event = calendar.getEventById(id);

                console.log(event);
                if (event) {
                    event.setProp('backgroundColor',  getBackgroundColorByStatus('DONE'));
                    event.setProp('borderColor', getBorderColorByStatus('DONE'));

                    notificationCompeted.style.display = 'block';
                    notificationCompeted.classList.add('show'); // Добавляем класс для анимации
                    setTimeout(() => {
                        // Скрываем уведомление через 2 секунды
                        notificationCompeted.style.display='none'
                        notificationCompeted.classList.remove('show'); // Удаляем класс для анимации
                    }, 4000);
                }

            })
            .catch(error => {
                // Обработка ошибки
                console.error('Error updating task:', error);
                const event = calendar.getEventById(id);

                console.log(event);

                if (event) {
                    event.setProp('backgroundColor',  getBackgroundColorByStatus('DONE'));
                    event.setProp('borderColor', getBorderColorByStatus('DONE'));
                    notificationCompeted.style.display = 'block';
                    notificationCompeted.classList.add('show'); // Добавляем класс для анимации
                    setTimeout(() => {
                        // Скрываем уведомление через 2 секунды
                        notificationCompeted.style.display='none'
                        notificationCompeted.classList.remove('show'); // Удаляем класс для анимации
                    }, 4000);
                }

            });
    });

    const deleteButton = document.getElementById('deleteButton');
    const notification = document.getElementById('notificationDelete');

    deleteButton.addEventListener('click', async function () {
        // Проверка, не был ли уже запущен процесс удаления

        // Отправка DELETE-запроса
        fetch(`/tasks/${id}`, {
            method: 'DELETE',
        })
            .then(response => {

                // Обработка успешного ответа от сервера
                console.log('Task deleted successfully');
                const event = calendar.getEventById(id);
                if (event) {
                    event.remove();
                }
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




    // Ваш обработчик нажатия на кнопку изменения

    console.log(groupId);
    // Загрузка задач из базы данных
    fetch(`/tasks/groups/${groupId}`)
        .then(response => response.json())
        .then(tasks => {
            calendar.setOption('eventContent', function(info) {
                const eventElement = document.createElement('div');
                eventElement.classList.add('p-2', 'bg-gradient-primary', 'text-white', 'rounded', 'shadow', 'd-flex', 'justify-content-between');

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
                    borderColor: getBorderColorByStatus(task.status)
                });
            });
            calendar.render();
        })
        .catch(error => {
            console.error('Error fetching tasks:', error);
        });



    document.getElementById('createTaskForm').addEventListener('submit', function(event) {
        event.preventDefault();

        const title = document.getElementById('title').value;
        const description = document.getElementById('description').value;
        const dateOfStartInput = document.getElementById('dateOfStart');
        const dateOfEndInput = document.getElementById('dateOfEnd');
        const notificationCreate = document.getElementById('notificationCreate');


        const dateOfStart = new Date(dateOfStartInput.value).toISOString(); // Преобразование в стандартный ISO8601 формат
        const dateOfEnd = new Date(dateOfEndInput.value).toISOString();
        const groupIdJson = document.getElementById('groupId');
        let userId = document.getElementById("selectedUserId").value;
        let groupId = groupIdJson.value;

        const taskData = {
            title: title,
            description: description,
            dateOfStart: dateOfStart,
            dateOfEnd: dateOfEnd,
            groupId: groupId,
            userId: userId,
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
                        console.log('Task added:', task);
                        console.log('Adding event:', task.id, task.userId, task.status, task.title, task.description, task.dateOfStart, task.dateOfEnd);
                        calendar.addEvent({
                            id: task.id,
                            title: task.title,
                            status: task.status,
                            userId: task.userId, // Добавляем userId в объект события
                            start: task.dateOfStart,
                            end: task.dateOfEnd,
                            display: 'block',
                            description: task.description,
                            backgroundColor: getBackgroundColorByStatus(task.status),
                            borderColor: getBorderColorByStatus(task.status)
                        });
                        calendar.render();
                        // Показываем уведомление плавно
                        notificationCreate.style.display = 'block';
                        notificationCreate.classList.add('show'); // Добавляем класс для анимации
                        setTimeout(() => {
                            // Скрываем уведомление через 2 секунды
                            notificationCreate.style.display='none'
                            notificationCreate.classList.remove('show'); // Удаляем класс для анимации
                        }, 4000);
                    })
                } else {
                    response.text().then( error => {
                        const notificationChange = document.getElementById('notificationErrorTaskCreate');
                        const er = document.getElementById('errorTask');
                        er.innerText=error;
                        notificationChange.style.display = 'block';
                        notificationChange.classList.add('show'); // Добавляем класс для анимации

                        setTimeout(() => {
                            // Скрываем уведомление через 2 секунды
                            notificationChange.style.display = 'none';
                            notificationChange.classList.remove('show'); // Удаляем класс для анимации

                            localStorage.removeItem('showNotification'); // Удаляем флаг из localStorage
                        }, 8000);
                    })
                }
            })

    });

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
            return "Создан"; // Синий цвет для CREATED
        case "IN_PROCESS":
            return "В процессе"; // Оранжевый цвет для IN_PROCESS
        case "DONE":
            return "Выполнено"; // Зеленый цвет для DONE
        case "NOT_DONE":
            return "Не выполнено"; // Красный цвет для NOT_DONE
    }
}






