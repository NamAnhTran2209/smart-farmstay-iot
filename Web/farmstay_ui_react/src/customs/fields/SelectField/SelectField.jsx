import PropTypes from 'prop-types';
import Form from 'react-bootstrap/Form';

SelectField.propTypes = {
    field: PropTypes.object.isRequired,
    form: PropTypes.object.isRequired,

    label: PropTypes.string,
    placeholder: PropTypes.string,
    description: PropTypes.string,
    disabled: PropTypes.bool,
    options: PropTypes.array,
};

SelectField.defaultProps = {
    label: '',
    placeholder: '',
    description: '',
    disabled: false,
    options: [],
};

function SelectField({ field, form, label, placeholder, description, disabled, options, ...props }) {
    const { name, value, onChange, onBlur } = field;

    return (
        <Form.Group className="mb-1">
            <Form.Label htmlFor={name}>{label}</Form.Label>
            <Form.Select id={name} name={name} value={value} onBlur={onBlur} onChange={onChange} disabled={disabled}>
                <option value={''}>{placeholder}</option>
                {Array.isArray(options) &&
                    options.map((option) => {
                        return (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        );
                    })}
            </Form.Select>
        </Form.Group>
    );
}

export default SelectField;
