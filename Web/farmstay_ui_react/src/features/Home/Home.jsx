import classNames from 'classnames/bind';

import Carousel from 'react-multi-carousel';
import 'react-multi-carousel/lib/styles.css';

import { DefaultLayout } from '~/layouts';
import styles from './Home.module.scss';
import images from '~/assets/images';
import { Button, Card } from 'react-bootstrap';
import { useEffect, useState } from 'react';
import { farmstayApi } from '~/api';
import { Link } from 'react-router-dom';
import config from '~/config';
const cx = classNames.bind(styles);

function Home() {
    const [farmstays, setFarmstays] = useState([]);
    useEffect(() => {
        farmstayApi.getAllFarmstay().then((res) => {
            setFarmstays(res.data);
        });
    }, []);

    return (
        <DefaultLayout>
            <div className={cx('wrapper')}>
                <div className={cx('intro')}>
                    <div className={cx('wrapper-image')}>
                        <img className={cx('image')} src={images.bgHome} alt="" />
                        <div className={cx('logo')}>
                            <svg width="132" height="132">
                                <image href={images.logo} width="132" height="132" />
                            </svg>
                        </div>
                        <h2 className={cx('title', 'fw-bold', 'text-white')}>"Đồ án tốt nghiệp DHDTMT15B"</h2>
                    </div>

                    <div className={cx('wrapper-carousel')}>
                        <Carousel
                            additionalTransfrom={0}
                            arrows
                            autoPlay
                            autoPlaySpeed={4000}
                            centerMode={false}
                            className=""
                            containerClass={cx('carousel')}
                            dotListClass=""
                            draggable
                            focusOnSelect={false}
                            infinite
                            itemClass=""
                            keyBoardControl
                            minimumTouchDrag={80}
                            pauseOnHover
                            renderArrowsWhenDisabled={false}
                            renderButtonGroupOutside={false}
                            renderDotsOutside={false}
                            responsive={{
                                desktop: {
                                    breakpoint: {
                                        max: 3000,
                                        min: 1024,
                                    },
                                    items: 1,
                                },
                            }}
                            rewind={false}
                            rewindWithAnimation={false}
                            rtl={false}
                            shouldResetAutoplay
                            showDots
                            sliderClass=""
                            slidesToSlide={1}
                            swipeable
                        >
                            <div className={cx('carousel-items')}>
                                <img
                                    draggable={false}
                                    className={cx('carousel-items-image')}
                                    src={images.banner1}
                                    alt=""
                                />
                            </div>
                            <div className={cx('carousel-items')}>
                                <img
                                    draggable={false}
                                    className={cx('carousel-items-image')}
                                    src={images.banner2}
                                    alt=""
                                />
                            </div>
                        </Carousel>
                    </div>
                </div>
                <div className={cx('wrapper-products')}>
                    <h2 className={cx('products-title')}>Danh sách Farmstay</h2>
                    <div className={cx('wrapper-carousel-products', 'my-4')}>
                        {farmstays.length > 0 ? (
                            <Carousel
                                additionalTransfrom={0}
                                arrows
                                autoPlaySpeed={3000}
                                centerMode={true}
                                className=""
                                containerClass={cx('carousel-products')}
                                dotListClass=""
                                draggable={false}
                                focusOnSelect={false}
                                infinite
                                itemClass=""
                                keyBoardControl
                                minimumTouchDrag={80}
                                pauseOnHover
                                renderArrowsWhenDisabled={false}
                                renderButtonGroupOutside={false}
                                renderDotsOutside={false}
                                responsive={{
                                    desktop: {
                                        breakpoint: {
                                            max: 3000,
                                            min: 1024,
                                        },
                                        items: 2,
                                        partialVisibilityGutter: 40,
                                    },
                                }}
                                rewind={false}
                                rewindWithAnimation={false}
                                rtl={false}
                                shouldResetAutoplay
                                showDots={false}
                                sliderClass=""
                                slidesToSlide={1}
                                swipeable
                            >
                                {farmstays.map((farmstay) => {
                                    return (
                                        <Card key={farmstay.uuid} style={{ width: '18rem' }}>
                                            <Card.Img
                                                variant="top"
                                                src={farmstay.images[0]}
                                                className={cx('card-image-products')}
                                                draggable={false}
                                            />
                                            <Card.Body>
                                                <Card.Title className="fw-bold">{farmstay.name}</Card.Title>
                                                <Card.Text className="text-danger">
                                                    {farmstay.rent_cost_per_day.toLocaleString('it-IT', {
                                                        style: 'currency',
                                                        currency: 'VND',
                                                    })}
                                                </Card.Text>
                                                <Link to={[config.routes.farmstays.path, farmstay.uuid].join('/')}>
                                                    <Button variant="success">Xem chi tiết</Button>
                                                </Link>
                                            </Card.Body>
                                        </Card>
                                    );
                                })}
                            </Carousel>
                        ) : (
                            <h2>Không còn farmstay nào :(</h2>
                        )}
                    </div>
                </div>
            </div>
        </DefaultLayout>
    );
}

export default Home;
