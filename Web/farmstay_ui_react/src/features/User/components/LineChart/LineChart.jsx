import moment from 'moment';
import 'moment/locale/vi';
import { useEffect, useState } from 'react';
import {
    LineChart as LineChartReChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    Legend,
    ReferenceLine,
} from 'recharts';

function LineChart({
    data,
    height,
    width,
    XAxisKey,
    YAxisKey,
    lineKey,
    danger = false,
    dangerMin,
    dangerMinLabel,
    dangerMaxLabel,
    dangerMax,
    showDangerLine = false,
    name,
}) {
    const [values, setValues] = useState(data);
    useEffect(() => {
        if (Array.isArray(data) && data.length > 0) {
            setValues(
                data.map((item) => {
                    const { timestamp } = item;
                    if (typeof timestamp === 'number') {
                        return Object.assign(item, { timestamp: moment(timestamp * 1000).format('lll:s') });
                    }
                    return item;
                }),
            );
        }
    }, [data]);
    return (
        <LineChartReChart
            width={500}
            height={222}
            data={values}
            margin={{
                top: 5,
                right: 5,
                left: 5,
                bottom: 5,
            }}
        >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey={XAxisKey} hide />
            <YAxis />
            <Tooltip cursor={{ strokeWidth: 1 }} />
            <Legend layout="horizontal" verticalAlign="top" align="right" wrapperStyle={{ top: -5, right: 10 }} />

            {showDangerLine && (
                <ReferenceLine y={dangerMax} label={dangerMaxLabel} stroke="#FF8A8A" strokeWidth={0.8} />
            )}
            {showDangerLine && (
                <ReferenceLine y={dangerMin} label={dangerMinLabel} stroke="#FF5C5C" strokeWidth={0.8} />
            )}

            <Line
                type="monotone"
                isAnimationActive={false}
                dataKey={lineKey}
                stroke={danger ? '#FF0000' : '#82ca9d'}
                strokeWidth={1.5}
                name={name}
            />
        </LineChartReChart>
    );
}

export default LineChart;
