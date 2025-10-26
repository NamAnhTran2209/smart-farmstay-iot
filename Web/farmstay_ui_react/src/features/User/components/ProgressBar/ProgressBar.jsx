import { CircularProgressbar, buildStyles } from 'react-circular-progressbar';
import 'react-circular-progressbar/dist/styles.css';

function Progressbar({ value, minValue, maxValue, text, className, danger = false, ...props }) {
    const normalStyle = {};
    const dangerStyle = {
        textColor: 'red',
        pathColor: 'red',
        trailColor: 'gold',
    };
    return (
        <CircularProgressbar
            value={value}
            minValue={minValue || 0}
            maxValue={maxValue || 100}
            text={`${text}`}
            className={className}
            styles={buildStyles(danger ? dangerStyle : normalStyle)}
            {...props}
        />
    );
}

export default Progressbar;
