document.addEventListener('DOMContentLoaded', function() {
    let id = 0;
    const calendarEl = document.getElementById('calendar');
    const calendar = new FullCalendar.Calendar(calendarEl, {

        selectable: true,
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
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
            const iconElement = document.getElementById('icon');
            const head = document.getElementById('head');


            id = event.id;
            var statusName = getStatus(event.extendedProps.status);


            eventTitle.textContent = event.title;
            eventDescription.textContent = event.extendedProps.description;
            eventDateStart.textContent = `Начало: ${event.start.toLocaleString()}`;
            eventDateEnd.textContent = `Конец: ${event.end.toLocaleString()}`;
            eventStatus.textContent = statusName;




            switch (statusName) {
                case "Создан":
                    iconElement.setAttribute('name', 'save-outline'); // Имя иконки для CREATED
                    eventStatus.style.color = '#6fb6fa'; // Изменение цвета текста
                    head.style.backgroundColor='#6fb6fa';
                    eventDateEnd.style.color = '#6fb6fa';
                    break;
                case "В процессе":
                    iconElement.setAttribute('name', 'construct-outline'); // Имя иконки для IN_PROCESS
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

            const editButton = eventModal.querySelector('.btn');
            editButton.addEventListener('click', editButtonClickHandler);

            document.getElementById('deleteButton').addEventListener('click', function () {
                // Отправка DELETE-запроса
                fetch(`/tasks/${id}`, {
                    method: 'DELETE',
                })
                    .then(response => {
                        if (response.ok) {
                            // Обработка успешного ответа от сервера
                            console.log('Task deleted successfully');
                            window.location.reload();
                            // Дополнительные действия, если необходимо
                        } else {
                            // Обработка ошибки
                            console.error('Error deleting task:', response.statusText);
                            window.location.reload();

                        }
                        // Дополнительные действия, если необходимо
                    })
                    .catch(error => {
                        // Обработка ошибки
                        console.error('Error deleting task:', error);
                        window.location.reload();

                    });
            });


            document.getElementById('completedButton').addEventListener('click',function () {
                const taskId = id; // замените на актуальный ID задачи
                const startDateFormatted = event.start.toISOString(); // Преобразование в формат "yyyy-MM-ddTHH:mm:ss.SSSX"
                const endDateFormatted = event.end.toISOString();

                const taskData = {
                    title: eventTitle.textContent,
                    description: eventDescription.textContent,
                    dateOfStart: startDateFormatted,
                    dateOfEnd: endDateFormatted,
                    status: 'DONE'
                };

                fetch(`/tasks/${taskId}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(taskData)
                })
                    .then(response => response.json())
                    .then(data => {
                        // Обработка успешного ответа от сервера
                        console.log('Task updated:', data);
                        window.location.reload();

                    })
                    .catch(error => {
                        // Обработка ошибки
                        console.error('Error updating task:', error);
                        window.location.reload();


                    });
            });


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
    // Ваш обработчик нажатия на кнопку изменения
    function editButtonClickHandler() {
        const eventTitle = document.getElementById('eventTitle');
        const eventDescription = document.getElementById('eventDescription');
        const eventDateStart = document.getElementById('eventDateStart');
        const eventDateEnd = document.getElementById('eventDateEnd');
        const changeButton = document.getElementById('editButton');

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

        eventTitle.textContent = '';
        eventTitle.appendChild(inputTitle);

        eventDescription.textContent = '';
        eventDescription.appendChild(inputDescription);

        eventDateStart.textContent = '';
        eventDateStart.appendChild(inputDateStart);

        eventDateEnd.textContent = '';
        eventDateEnd.appendChild(inputDateEnd);


        changeButton.addEventListener("click", function (event) {
            event.preventDefault();

            const taskId = id; // замените на актуальный ID задачи
            const taskData = {
                title: inputTitle.value,
                description: inputDescription.value,
                dateOfStart: new Date(inputDateStart.value).toISOString(),
                dateOfEnd: new Date(inputDateEnd.value).toISOString()
            };

            fetch(`/tasks/${taskId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(taskData)
            })
                .then(response => response.json())
                .then(data => {
                    // Обработка успешного ответа от сервера
                    console.log('Task updated:', data);
                    window.location.reload();
                })
                .catch(error => {
                    // Обработка ошибки
                    console.error('Error updating task:', error);
                    window.location.reload();
                });
        });
    }
    // Загрузка задач из базы данных
    fetch('/tasks')
        .then(response => response.json())
        .then(tasks => {
            tasks.forEach(task => {
                console.log('Adding event:',task.id,task.status, task.title, task.description, task.dateOfStart, task.dateOfEnd);
                calendar.addEvent({
                    id: task.id,
                    title: task.title,
                    allDay: task.allDay,
                    status: task.status,
                    start: task.dateOfStart,
                    end: task.dateOfEnd,
                    display:'block',
                    description: task.description,
                    backgroundColor: getBackgroundColorByStatus(task.status),
                    borderColor: 'rgba(255,255,255,0)'
                });
            });
            calendar.render();
        })
        .catch(error => {
            console.error('Error fetching tasks:', error);
        });

    const createTaskForm = document.getElementById('createTaskForm');
    createTaskForm.addEventListener('submit', function(event) {
        event.preventDefault();

        const formData = new FormData(createTaskForm);
        const task = {};
        formData.forEach((value, key) => {
            task[key] = value;
        });

        const startDate = new Date(task.dateOfStart); // Преобразование строки в объект Date
        const endDate = new Date(task.dateOfEnd); // Преобразование строки в объект Date

        calendar.addEvent({
            id: task.id,
            title: task.title,
            start: startDate,
            end: endDate,
            display:'block',
            description: task.description,
            backgroundColor: getBackgroundColorByStatus('CREATED'),
            borderColor: 'rgba(255,255,255,0)'

        });

        createTaskForm.reset();
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



document.getElementById('createTaskForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const title = document.getElementById('title').value;
    const description = document.getElementById('description').value;
    const dateOfStartInput = document.getElementById('dateOfStart');
    const dateOfEndInput = document.getElementById('dateOfEnd');
    const allDay = document.getElementById('allDay').checked;

    const dateOfStart = new Date(dateOfStartInput.value).toISOString(); // Преобразование в стандартный ISO8601 формат
    const dateOfEnd = new Date(dateOfEndInput.value).toISOString();

    const taskData = {
        title: title,
        description: description,
        dateOfStart: dateOfStart,
        dateOfEnd: dateOfEnd,
        allDay: allDay
    };

    fetch('/tasks', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(taskData)
    })
        .then(response => response.json())
        .then(data => {
            console.log('Task added:', data);
            window.location.reload();
            // Дополнительные действия после успешного добавления задачи
        })
        .catch(error => {
            console.error('Error adding task:', error);
            // Обработка ошибки, если что-то пошло не так
        });
});