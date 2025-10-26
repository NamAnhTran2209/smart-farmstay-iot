import classNames from 'classnames/bind';
import { Formik, Form as FormFormik, FastField } from 'formik';
import MessageField from '~/customs/MessageField';
import styles from './FormSendMessage.module.scss';
import { Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPaperPlane } from '@fortawesome/free-solid-svg-icons';

const cx = classNames.bind(styles);

function FormSendMessage({ onSubmit, token, currentConversation }) {
    return (
        <Formik
            initialValues={{
                message: '',
            }}
            onSubmit={onSubmit}
        >
            {(formikProps) => {
                return (
                    <FormFormik className={cx('wrapper')}>
                        <div className={cx('input')}>
                            <FastField
                                name="message"
                                component={MessageField}
                                //
                                type="text"
                                placeholder="Nhập tin nhắn"
                            ></FastField>
                        </div>
                        <Button type="submit" className={cx('button')}>
                            <FontAwesomeIcon icon={faPaperPlane} />
                        </Button>
                    </FormFormik>
                );
            }}
        </Formik>
    );
}

export default FormSendMessage;
