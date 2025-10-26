import classNames from 'classnames/bind';
import 'react-circular-progressbar/dist/styles.css';
import styles from './AreaVisualization.module.scss';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTreeCity } from '@fortawesome/free-solid-svg-icons';
import { Col, Row } from 'react-bootstrap';
import VisualizationGroup from '../VisualizationGroup/VisualizationGroup';

const cx = classNames.bind(styles);

function AreaVisualization({ name, fields }) {
    return (
        <div className={cx('wrapper', 'mt-4')}>
            <div className={cx('header')}>
                <h5 className="fw-bold">
                    <FontAwesomeIcon icon={faTreeCity} /> Khu vực {(name + '').toLocaleUpperCase()}
                </h5>
            </div>
            <div className={cx('body')}>
                <Row>
                    {Array.from(fields)
                        .sort((a, b) => a.visualization.localeCompare(b.visualization) * -1)
                        .map((field) => {
                            return (
                                <Col key={field.field_name} xl={6} className="mt-3">
                                    <VisualizationGroup field={field} />
                                </Col>
                            );
                        })}
                </Row>
            </div>
        </div>
    );
}

export default AreaVisualization;
