import Tippy from '@tippyjs/react/headless'; // different import path!
import { Wrapper as PopperWrapper } from '~/components/Popper';
import classNames from 'classnames/bind';
import PropTypes from 'prop-types';

import styles from './Menu.module.scss';
import MenuItem from './MenuItem';
import Header from './Header';
import { useState } from 'react';

const cx = classNames.bind(styles);
// const defaultFn = () => {};
function Menu({ children, items = [], hideOnClick = false, onChange }) {
    const [history, setHistory] = useState([{ data: items }]);
    const current = history[history.length - 1];
    const renderItems = () => {
        return current.data.map((item, index) => {
            const isParent = item.hasOwnProperty('children');

            return (
                <MenuItem
                    key={index}
                    data={item}
                    onClick={() => {
                        if (isParent) {
                            setHistory((prev) => [...prev, item.children]);
                        } else {
                            item.onClick();
                        }
                    }}
                />
            );
        });
    };

    const handleBack = () => {
        setHistory((prev) => prev.slice(0, history.length - 1));
    };
    const renderResult = (attrs) => {
        return (
            <div className={cx('menu-list')} tabIndex="-1" {...attrs}>
                <PopperWrapper className={cx('menu-popper')}>
                    {history.length > 1 && <Header title={current.title} onBack={handleBack} />}
                    <div className={cx('menu-body')}>{renderItems()}</div>
                </PopperWrapper>
            </div>
        );
    };

    const handleResetMenu = () => {
        setHistory((prev) => prev.slice(0, 1));
    };

    return (
        <Tippy
            interactive
            delay={[0, 400]}
            placement="bottom-end"
            offset={[12, 6]}
            render={renderResult}
            onHide={handleResetMenu}
            hideOnClick={hideOnClick}
        >
            {children}
        </Tippy>
    );
}

Menu.propTypes = {
    children: PropTypes.node,
    items: PropTypes.array,
    hideOnClick: PropTypes.bool,
    onChange: PropTypes.func,
};

export default Menu;
