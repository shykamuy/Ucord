import './NotificationsSectionStudent.scss'
import ChatModalSection from '../../Modal/ChatModalSection/ChatModalSection.jsx'
import AskModalSection from '../../Modal/AskModalSection/AskModalSection.jsx';
import { useState } from 'react';

export default function NotificationsSection() {
    const [isModalChatOpen, setIsModalChatlOpen] = useState(false);
    const [isModalAskOpen, setIsModalAskOpen] = useState(false);


    const openModalChat = () => {
        setIsModalChatlOpen(true);
    }

    const closeModalChat = () => {
        setIsModalChatlOpen(false);
    }

    const openModalAsk = () => {
        setIsModalAskOpen(true);
    }

    const closeModalAsk = () => {
        setIsModalAskOpen(false);
    }

    return (
        <main className='notifications'>

            {isModalChatOpen && <ChatModalSection onClickClose={closeModalChat} />}
            {isModalAskOpen && <AskModalSection onClickClose={closeModalAsk} />}

            <section className='block-ad'>

                <ul className='ad-list'>
                    <li className='ad-item'>
                        <div className='title-container'>
                            <h3>Изменение расписания занятий по Программированию</h3>
                            <button type='button' >Подробнее</button>
                        </div>
                        <p className='description'>В связи с проведением технических работ в аудитории 314,
                            изменяется расписание занятий по Программированию для группы РТФ-212.
                            Новое расписание: Занятия 26 октября будут проходить в аудитории 205.
                            Уважаемые студенты группы РТФ-212!  Сообщаем вам, что в связи с проведением
                            технических работ в аудитории 314...</p>
                    </li>


                </ul>

            </section>

            <section className='block-requests'>
                <ul className='requests-list'>

                    <li className='requests-item'>
                        <div className='title-container'>
                            <h3>Проблема с доступом к университетской сети</h3>
                            <button className='btn-status resolve'>Решается</button>
                        </div>
                        <p className='description'>Уважаемый [Имя куратора], у меня возникли
                            проблемы с доступом к университетской сети Wi-Fi. Я не могу подключиться,
                            скорость очень низкая. Куда мне обратиться для решения данной проблемы?</p>
                        <div className='btn-wrapper'>
                            <button className='btn-chat' type='button' onClick={openModalChat}>Чат</button>
                        </div>
                    </li>

                    <li className='requests-item'>
                        <div className='title-container'>
                            <h3>Вопрос по поводу изменения расписания занятий</h3>
                            <button className='btn-status decided'>Решено</button>
                        </div>
                        <p className='description'>Уважаемый куратор, у меня возникли вопросы по поводу изменений в расписании
                            занятий по Программированию для группы РТФ-212. Мне стало известно, что занятия 26 октября
                            будут проходить...
                        </p>
                        <div className='btn-wrapper'>
                            <button className="btn-deleted" type='button' ><img src='../../../../public/trash.svg' /></button>
                            <button className='btn-chat' type='button' onClick={openModalChat}>Чат</button>
                        </div>
                    </li>

                    <li className='requests-item'>
                        <div className='title-container'>
                            <h3>Необходимость уточнения деталей переноса занятий</h3>
                            <button className='btn-status expected'>Ожидает принятия</button>
                        </div>
                        <p className='description'>Здравствуйте, уважаемый куратор! Я хотел бы получить дополнительные разъяснения по поводу переноса занятий по Программированию....</p>
                        <div className='btn-wrapper'>
                            <button className="btn-deleted" type='button' ><img src='../../../../public/trash.svg' /></button>
                        </div>
                    </li>

                </ul>

                <div className='btn-wrapper'>
                    <button type='button' className='btn-question' onClick={openModalAsk}>Спросить</button>
                </div>
            </section>
        </main >
    )
}