
import logo from '../Pictures/logo.png';
function Title({ title }) {
    return (
        <div id="open" className="col-12">
            <img id="logo" src={logo} alt="logo"></img>
            <h1 className="col-10">{title}</h1>
        </div>
    );
}

export default Title;