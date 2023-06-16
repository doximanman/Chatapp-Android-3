import {Link} from "react-router-dom";

function BottomMessage( {description} ) {
    return (
        <div id={description.id}>{description.question}<Link to={description.link}>{description.click}</Link>{description.goal}</div>
    );
}

export default BottomMessage;