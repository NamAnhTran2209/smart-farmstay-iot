import HeadlessTippy from '@tippyjs/react/headless';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCircleXmark, faMagnifyingGlass, faSpinner } from '@fortawesome/free-solid-svg-icons';
import { useEffect, useRef, useState } from 'react';
import { Wrapper as PopperWrapper } from '~/components/Popper';
import classNames from 'classnames/bind';
import * as searchService from '~/services/api/searchService';

import SearchItem from '~/components/SearchItem';
import styles from './Search.module.scss';
import { useDebounce } from '~/hooks';
const cx = classNames.bind(styles);
function Search() {
    const [searchValue, setSearchValue] = useState('');
    const [searchResult, setSearchResult] = useState([]);
    const [showResult, setShowResult] = useState(false);
    const [loading, setLoading] = useState(false);

    const deboucedValue = useDebounce(searchValue, 500);
    const inputSearchRef = useRef();

    const handleHideResult = () => {
        setShowResult(false);
    };

    const handleClear = () => {
        setSearchValue('');
        setSearchResult([]);
        inputSearchRef.current.focus();
    };

    useEffect(() => {
        if (!deboucedValue.trim()) {
            setSearchResult([]);
            return;
        }

        const fetchApi = async () => {
            try {
                setLoading(true);
                const data = await searchService.search(deboucedValue);
                if (data && data.length > 0) {
                    setSearchResult(data);
                    setLoading(false);
                }
            } catch (error) {
                setLoading(false);
            }
        };
        fetchApi();
    }, [deboucedValue]);
    return (
        // Disable waring Tippy
        <div>
            <HeadlessTippy
                visible={showResult && searchResult.length > 0}
                interactive
                appendTo={() => document.body} // Disable waring Tippy
                render={(attrs) => {
                    return (
                        <div className={cx('search-result')} tabIndex="-1" {...attrs}>
                            <PopperWrapper>
                                <h4 className={cx('search-title')}>Farmstays</h4>
                                {searchResult.map((result) => {
                                    return <SearchItem data={result} key={result.id}></SearchItem>;
                                })}
                            </PopperWrapper>
                        </div>
                    );
                }}
                onClickOutside={handleHideResult}
            >
                <div className={cx('search')}>
                    <input
                        ref={inputSearchRef}
                        value={searchValue}
                        type="text"
                        placeholder="Tìm kiếm Farmstay"
                        spellCheck={false}
                        onChange={(e) => {
                            const value = e.target.value;
                            if (!value.startsWith(' ')) {
                                setSearchValue(value.trim());
                            }
                        }}
                        onFocus={(e) => {
                            setShowResult(true);
                        }}
                    />
                    {!!searchValue && !loading && (
                        <button className={cx('clear')} onClick={handleClear}>
                            <FontAwesomeIcon icon={faCircleXmark} />
                        </button>
                    )}

                    {loading && <FontAwesomeIcon className={cx('loading')} icon={faSpinner} />}

                    <button
                        className={cx('search-btn')}
                        onMouseDown={(e) => {
                            e.preventDefault();
                        }}
                    >
                        <FontAwesomeIcon icon={faMagnifyingGlass} />
                    </button>
                </div>
            </HeadlessTippy>
        </div>
    );
}

export default Search;
